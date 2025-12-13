package com.torchbell.lovecoach.npc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "채팅 생성 요청")
public class ChatTalkRequest {
    private Long npcId;
    private String message;
}
