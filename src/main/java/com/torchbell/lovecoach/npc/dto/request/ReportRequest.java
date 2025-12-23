package com.torchbell.lovecoach.npc.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class ReportRequest {

    @Schema(description = "NPC ID (1: Diet, 2: Muscle, 3: Cardio)", example = "1")
    private Long npcId;

    @Schema(description = "분석할 연도", example = "2024")
    private int year;

    @Schema(description = "분석할 월", example = "12")
    private int month;
}
