package com.torchbell.lovecoach.npc.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Npc 목록 정보 응답")
public class NpcInfoResponse {
    private Long npcId;
    private String name;
    private String personality;
    private Integer affectionScore;
    private String currentState;



}
