package com.torchbell.lovecoach.npc.service;

import com.torchbell.lovecoach.common.constant.BusinessConstant;
import com.torchbell.lovecoach.npc.dao.NpcDao;
import com.torchbell.lovecoach.npc.dto.request.ChatLogRequest;
import com.torchbell.lovecoach.npc.dto.request.ChatTalkRequest;
import com.torchbell.lovecoach.npc.dto.response.ChatLogResponse;
import com.torchbell.lovecoach.npc.dto.response.ChatTalkResponse;
import com.torchbell.lovecoach.npc.dto.response.NpcInfoResponse;
import com.torchbell.lovecoach.npc.model.ChatLog;
import com.torchbell.lovecoach.npc.model.Npc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NpcService {

    private final NpcDao npcDao;
    private final AiChatService aiChatService;

    // npc 목록 조회
    public List<NpcInfoResponse> getNpcInfoList(Long userId) {
        return npcDao.selectNpcListByUserId(userId);
    }

    // 채팅 내역 조회
    public List<ChatLogResponse> getChatLogList(Long userId, ChatLogRequest request) {

        // 순수 Mybatis에는 Pagination 기능을 제공하지 않아서 limit, offset을 직접 계산해줌
        int limit = request.getSize();
        int offset = (request.getPage()-1)*limit;

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
                BusinessConstant.CONTEXT_LENGTH
        );

        // 첫 채팅인 경우(DB에 채팅 기록이 없는경우) 예외처리
        String context = chatLogList.isEmpty()
                ? "첫 채팅입니다."
                : chatLogList.get(0).getContext();

        // AI 답변 생성
        String messageAi = aiChatService.getChat(npc.getName(), context, chatLogList, request.getMessage());

        // DB에 저장하기
        ChatLog newChatLog = ChatLog.builder()
                        .userId(userId)
                        .npcId(npc.getNpcId())
                        .messageUser(request.getMessage())
                        .messageAi(messageAi)
                        .createdAt(LocalDateTime.now())
                        .build();

        npcDao.insertChatLog(newChatLog);
        return ChatTalkResponse.fromEntity(newChatLog);
    }

}
