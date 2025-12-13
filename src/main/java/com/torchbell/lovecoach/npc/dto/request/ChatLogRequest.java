package com.torchbell.lovecoach.npc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 목록 조회 요청")
public class ChatLogRequest {
    private Long npcId;
    private Integer page = 1;
    private Integer size = 20;
}
