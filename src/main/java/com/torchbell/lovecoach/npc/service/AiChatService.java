package com.torchbell.lovecoach.npc.service;

import com.torchbell.lovecoach.npc.model.ChatLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiChatService {

    // 문맥 만들어주는 메서드
    public String getContext(String npcName,String context, List<ChatLog> chatLogList){
        // 아직은 스텁 메서드임
        return "Npc 채팅 문맥을 생성해주는 Mock 데이터 입니다.";
    }

    // 채팅 답변해주는 메서드
    public String getChat(String npcName, String context, List<ChatLog> chatLogList, String newChat){
        // 아직은 스텁 메서드임
        return "Npc 채팅 답변 Mock 데이터 입니다.";
    }
}
