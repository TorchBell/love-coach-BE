package com.torchbell.lovecoach.npc.service;

import com.torchbell.lovecoach.cardio.dao.CardioDao;
import com.torchbell.lovecoach.cardio.model.CardioLog;
import com.torchbell.lovecoach.common.constant.BusinessConstant;
import com.torchbell.lovecoach.food.dao.FoodDao;
import com.torchbell.lovecoach.food.model.UserFood;
import com.torchbell.lovecoach.muscle.dao.MuscleDao;
import com.torchbell.lovecoach.muscle.model.MuscleLog;
import com.torchbell.lovecoach.npc.dao.NpcDao;
import com.torchbell.lovecoach.npc.dto.request.ChatLogRequest;
import com.torchbell.lovecoach.npc.dto.request.ChatTalkRequest;
import com.torchbell.lovecoach.npc.dto.request.ReportRequest;
import com.torchbell.lovecoach.npc.dto.response.ChatLogResponse;
import com.torchbell.lovecoach.npc.dto.response.ChatTalkResponse;
import com.torchbell.lovecoach.npc.dto.response.NpcInfoResponse;
import com.torchbell.lovecoach.npc.event.AffinityChangedEvent;
import com.torchbell.lovecoach.npc.model.ChatLog;
import com.torchbell.lovecoach.npc.model.Npc;
import com.torchbell.lovecoach.npc.model.UserNpc;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NpcService {

    private final NpcDao npcDao;
    private final AiChatService aiChatService;
    private final FoodDao foodDao;
    private final MuscleDao muscleDao;
    private final CardioDao cardioDao;
    private final ApplicationEventPublisher eventPublisher;

    // npc 목록 조회
    public List<NpcInfoResponse> getNpcInfoList(Long userId) {
        return npcDao.selectNpcListByUserId(userId);
    }

    // 채팅 내역 조회
    public List<ChatLogResponse> getChatLogList(Long userId, ChatLogRequest request) {

        // 순수 Mybatis에는 Pagination 기능을 제공하지 않아서 limit, offset을 직접 계산해줌
        int limit = request.getSize();
        int offset = (request.getPage() - 1) * limit;

        return npcDao.selectChatLogListByUserIdAndNpcId(userId, request.getNpcId(), limit, offset)
                .stream()
                .map(ChatLogResponse::fromEntity)
                .toList();
    }

    // 채팅(대화하기)
    @Transactional
    public ChatTalkResponse getChatTalk(Long userId, ChatTalkRequest request) {
        // npc 이름, context, 채팅기록 넘겨줘야함

        Npc npc = npcDao.selectNpcById(request.getNpcId());

        List<ChatLog> chatLogList = npcDao.selectChatLogListByUserIdAndNpcId(
                userId, // 유저
                request.getNpcId(), // npc
                0,
                BusinessConstant.CONTEXT_LENGTH);

        // 첫 채팅인 경우(DB에 채팅 기록이 없는경우) 예외처리
        String context = chatLogList.isEmpty()
                ? "이 사용자와의 첫 만남입니다. 반갑게 맞아주세요."
                : chatLogList.get(0).getContext();

        // AI 답변 생성
        String systemInstruction = String.format("네 이름은 '%s'야. %s", npc.getName(), npc.getPersonality());
        String messageAi = aiChatService.getChat(
                systemInstruction,
                context,
                chatLogList,
                request.getMessage());
        // 새로운 문맥 생성
        String newContext = aiChatService.getContext(
                context,
                chatLogList,
                request.getMessage(),
                messageAi);

        // DB에 저장하기
        ChatLog newChatLog = ChatLog.builder()
                .userId(userId)
                .npcId(npc.getNpcId())
                .messageUser(request.getMessage())
                .messageAi(messageAi)
                .context(newContext)
                .createdAt(LocalDateTime.now())
                .build();
        if(!newChatLog.getMessageAi().equals("FastAPI 서버 연결 실패 또는 응답 오류")){
            npcDao.insertChatLog(newChatLog);
        }
        return ChatTalkResponse.fromEntity(newChatLog);
    }

    @Transactional

    // 식단기록  -> 토마 호감도 1증가
    // 근력 운동 기록 -> 벨 호감도 1 증가
    // 유산소 -> 치에 호감도 1 증가
    public void increaseAffinity(Long userId, Long npcId, int amount) {
        UserNpc userNpc = npcDao.selectUserNpc(userId, npcId);
        if (userNpc != null) {
            userNpc.setAffectionScore(userNpc.getAffectionScore() + amount);
            npcDao.updateUserNpc(userNpc);
            eventPublisher.publishEvent(new AffinityChangedEvent(
                    userId,
                    npcId,
                    userNpc.getAffectionScore()));
        }
    }

    // 분석 리포트 생성
    @Transactional(readOnly = true)
    public String createReport(Long userId, ReportRequest request) {
        // 1. NPC에 따른 리포트 타입 및 데이터 조회
        Long npcId = request.getNpcId();
        int year = request.getYear();
        int month = request.getMonth();

        String reportType = "";
        List<?> logs = null;
        Map<String, Object> statistics = new HashMap<>();

        if (npcId == 1L) { // 토마 - 식단
            reportType = "DIET";
            List<UserFood> foodLogs = foodDao.selectUserFoodList(userId, year, month);
            logs = foodLogs;
            statistics = calculateStatistics(foodLogs, "DIET");
        } else if (npcId == 2L) { // 벨 - 근력
            reportType = "MUSCLE";
            List<MuscleLog> muscleLogs = muscleDao.selectMuscleLogList(userId, year, month);
            logs = muscleLogs;
            statistics = calculateStatistics(muscleLogs, "MUSCLE");
        } else if (npcId == 3L) { // 치에 - 유산소
            reportType = "CARDIO";
            List<CardioLog> cardioLogs = cardioDao.selectCardioLogList(userId, year, month);
            logs = cardioLogs;
            statistics = calculateStatistics(cardioLogs, "CARDIO");
        } else {
            throw new IllegalArgumentException("유효하지 않은 NPC ID입니다.");
        }
        // 2. AI 분석 요청
        return aiChatService.getReport(reportType, logs, statistics);
    }

    private Map<String, Object> calculateStatistics(List<?> logs, String type) {
        Map<String, Object> stats = new HashMap<>();
        // TODO: 추후 구체적인 통계 로직 구현 필요
        // 현재는 스텁으로 빈 값 또는 기본값만 반환
        stats.put("total_count", logs.size());
        stats.put("analysis_date", LocalDateTime.now().toString());
        return stats;
    }



}
