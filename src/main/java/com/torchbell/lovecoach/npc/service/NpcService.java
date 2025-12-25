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
import com.torchbell.lovecoach.user.dao.UserDao;
import com.torchbell.lovecoach.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final UserDao userDao;

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
        String systemInstruction = String.format(
                "네 이름은 '%s'야. %s , 사용자의 응답에 대해서 40자 이내로 간결하게 답변해줘",
                npc.getName(),
                npc.getPersonality());

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
        if (!newChatLog.getMessageAi().equals("FastAPI 서버 연결 실패 또는 응답 오류")) {
            npcDao.insertChatLog(newChatLog);
        }
        return ChatTalkResponse.fromEntity(newChatLog);
    }

    @Transactional

    // 식단기록 -> 토마 호감도 1증가
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

        String reportType;
        List<?> logs;
        Map<String, Object> statistics;

        if (npcId == 1L) { // 토마 - 식단
            reportType = "DIET";
            List<UserFood> foodLogs = foodDao.selectUserFoodList(userId, year, month);
            logs = foodLogs;
            statistics = calculateStatistics(userId, foodLogs, "DIET");
        } else if (npcId == 2L) { // 벨 - 근력
            reportType = "MUSCLE";
            List<MuscleLog> muscleLogs = muscleDao.selectMuscleLogList(userId, year, month);
            logs = muscleLogs;
            statistics = calculateStatistics(userId, muscleLogs, "MUSCLE");
        } else if (npcId == 3L) { // 치에 - 유산소
            reportType = "CARDIO";
            List<CardioLog> cardioLogs = cardioDao.selectCardioLogList(userId, year, month);
            logs = cardioLogs;
            statistics = calculateStatistics(userId, cardioLogs, "CARDIO");
        } else {
            throw new IllegalArgumentException("유효하지 않은 NPC ID입니다.");
        }

        // 2. AI 분석 요청
        return aiChatService.getReport(reportType, logs, statistics);
    }

    private Map<String, Object> calculateStatistics(Long userId, List<?> logs, String type) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total_count", logs.size());
        stats.put("analysis_date", LocalDateTime.now().toString());

        if ("DIET".equals(type)) {
            calculateDietStatistics(userId, (List<UserFood>) logs, stats);
        } else if ("MUSCLE".equals(type)) {
            calculateMuscleStatistics((List<MuscleLog>) logs, stats);
        } else if ("CARDIO".equals(type)) {
            calculateCardioStatistics((List<CardioLog>) logs, stats);
        }

        return stats;
    }

    private void calculateDietStatistics(Long userId, List<UserFood> logs, Map<String, Object> stats) {
        User user = userDao.selectUserById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        String gender = user.getGender() != null ? user.getGender().toUpperCase() : "M";

        // Reference values
        Map<String, Double> refValues = new HashMap<>();
        if ("F".equals(gender)) {
            refValues.put("kcal", 1704.86);
            refValues.put("protein", 66.25);
            refValues.put("fat", 56.71);
            refValues.put("carb", 214.58);
            refValues.put("sugar", 58.6);
        } else { // Default Male
            refValues.put("kcal", 2295.8);
            refValues.put("protein", 94.03);
            refValues.put("fat", 74.04);
            refValues.put("carb", 284.05);
            refValues.put("sugar", 62.4);
        }
        stats.put("reference", refValues);

        // Calculate User Averages
        BigDecimal totalKcal = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalFat = BigDecimal.ZERO;
        BigDecimal totalCarb = BigDecimal.ZERO;
        BigDecimal totalSugar = BigDecimal.ZERO;

        long uniqueDays = logs.stream()
                .map(UserFood::getDate)
                .distinct()
                .count();

        for (UserFood log : logs) {
            BigDecimal quantity = log.getQuantity() != null ? log.getQuantity() : BigDecimal.ONE;
            BigDecimal weight = log.getWeight() != null ? log.getWeight() : BigDecimal.ZERO;
            BigDecimal ratio = weight.multiply(quantity).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

            if (log.getCalory() != null)
                totalKcal = totalKcal.add(log.getCalory().multiply(ratio));
            if (log.getProtein() != null)
                totalProtein = totalProtein.add(log.getProtein().multiply(ratio));
            if (log.getFat() != null)
                totalFat = totalFat.add(log.getFat().multiply(ratio));
            if (log.getCarb() != null)
                totalCarb = totalCarb.add(log.getCarb().multiply(ratio));
            if (log.getSugar() != null)
                totalSugar = totalSugar.add(log.getSugar().multiply(ratio));
        }

        Map<String, Double> userAvg = new HashMap<>();
        if (uniqueDays > 0) {
            BigDecimal days = BigDecimal.valueOf(uniqueDays);
            userAvg.put("kcal", totalKcal.divide(days, 2, RoundingMode.HALF_UP).doubleValue());
            userAvg.put("protein", totalProtein.divide(days, 2, RoundingMode.HALF_UP).doubleValue());
            userAvg.put("fat", totalFat.divide(days, 2, RoundingMode.HALF_UP).doubleValue());
            userAvg.put("carb", totalCarb.divide(days, 2, RoundingMode.HALF_UP).doubleValue());
            userAvg.put("sugar", totalSugar.divide(days, 2, RoundingMode.HALF_UP).doubleValue());
        } else {
            userAvg.put("kcal", 0.0);
            userAvg.put("protein", 0.0);
            userAvg.put("fat", 0.0);
            userAvg.put("carb", 0.0);
            userAvg.put("sugar", 0.0);
        }
        stats.put("user_average", userAvg);
        stats.put("analyzed_days", uniqueDays);
    }

    private void calculateMuscleStatistics(List<MuscleLog> logs, Map<String, Object> stats) {
        long uniqueDays = logs.stream()
                .map(MuscleLog::getDate)
                .distinct()
                .count();

        BigDecimal totalVolume = BigDecimal.ZERO;
        int totalSets = 0;

        for (MuscleLog log : logs) {
            int sets = log.getSetCount() != null ? log.getSetCount() : 0;
            int reps = log.getRepsPerSet() != null ? log.getRepsPerSet() : 0;
            BigDecimal weight = log.getWeight() != null ? log.getWeight() : BigDecimal.ZERO;

            totalSets += sets;
            // 볼륨 = 세트 * 횟수 * 무게
            BigDecimal volume = weight.multiply(BigDecimal.valueOf((long) sets * reps));
            totalVolume = totalVolume.add(volume);
        }

        stats.put("total_volume", totalVolume);
        stats.put("total_sets", totalSets);
        stats.put("start_date", logs.isEmpty() ? null : logs.get(logs.size() - 1).getDate()); // 정렬이 최신순이라면
        stats.put("end_date", logs.isEmpty() ? null : logs.get(0).getDate());
        stats.put("analyzed_days", uniqueDays);
    }

    private void calculateCardioStatistics(List<CardioLog> logs, Map<String, Object> stats) {
        long uniqueDays = logs.stream()
                .map(CardioLog::getDate)
                .distinct()
                .count();

        BigDecimal totalBurnedKcal = BigDecimal.ZERO;
        int totalDurationMinutes = 0;

        for (CardioLog log : logs) {
            if (log.getBurnedKcal() != null) {
                totalBurnedKcal = totalBurnedKcal.add(log.getBurnedKcal());
            }
            if (log.getDurationMinutes() != null) {
                totalDurationMinutes += log.getDurationMinutes();
            }
        }

        stats.put("total_burned_kcal", totalBurnedKcal);
        stats.put("total_duration_minutes", totalDurationMinutes);
        stats.put("analyzed_days", uniqueDays);
    }

}
