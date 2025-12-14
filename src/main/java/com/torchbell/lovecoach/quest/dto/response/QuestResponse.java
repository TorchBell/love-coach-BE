package com.torchbell.lovecoach.quest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestResponse {
    private Long questId;
    private String title;
    private String content;
    private Long npcId;
    private Integer credit;
    private Boolean isAccepted;
    private Boolean isAchieved;
}
