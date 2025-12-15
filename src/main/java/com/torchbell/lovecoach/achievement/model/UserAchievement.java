package com.torchbell.lovecoach.achievement.model;

import com.torchbell.lovecoach.common.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class UserAchievement extends BaseEntity {
    private Long userAchievementId;
    private Long userId;
    private Long achievementId;
    private int currentValue;
    private LocalDateTime achievedAt;
}
