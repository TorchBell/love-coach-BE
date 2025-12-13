package com.torchbell.lovecoach.quest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Quest {
    private Long questId;
    private String title;
    private String content;
    private Long npcId;
    private Integer credit;
}
