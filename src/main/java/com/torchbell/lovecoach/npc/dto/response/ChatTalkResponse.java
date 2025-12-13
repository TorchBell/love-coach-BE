package com.torchbell.lovecoach.npc.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.torchbell.lovecoach.npc.model.ChatLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(description = "채팅 생성 응답")
public class ChatTalkResponse {
    private Long chatId;
    private String messageUser;
    private String messageAi;

    // DB에 들어가기 전에 날짜가 생성되기때문에 쿼리 한 번 더 날리는 비효율 대신에 DTO 자체에서 포메팅 해줌
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static ChatTalkResponse fromEntity(ChatLog chatLog) {
        return ChatTalkResponse.builder()
                .chatId(chatLog.getChatId())
                .messageUser(chatLog.getMessageUser())
                .messageAi(chatLog.getMessageAi())
                .createdAt(chatLog.getCreatedAt())
                .build();
    }
}
