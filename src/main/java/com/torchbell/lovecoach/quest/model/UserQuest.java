package com.torchbell.lovecoach.quest.model;

import com.torchbell.lovecoach.common.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class UserQuest extends BaseEntity {
    private Long userQuestId;
    private Long userId;
    private Long questId;
    private Boolean isAchieved;
}
