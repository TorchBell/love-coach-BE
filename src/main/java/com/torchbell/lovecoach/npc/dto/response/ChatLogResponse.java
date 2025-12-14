package com.torchbell.lovecoach.npc.dto.response;

import com.torchbell.lovecoach.npc.model.ChatLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 목록 조회 응답")
public class ChatLogResponse {
    private Long chatId;
    private String messageUser;
    private String messageAi;
    private LocalDateTime createdAt;

    // chatLog 엔티티를 dto로 변환해주는 헬퍼메서드
    public static ChatLogResponse fromEntity(ChatLog chatLog) {
        return ChatLogResponse.builder()
                .chatId(chatLog.getChatId())
                .messageUser(chatLog.getMessageUser())
                .messageAi(chatLog.getMessageAi())
                .createdAt(chatLog.getCreatedAt())
                .build();
    }

}
