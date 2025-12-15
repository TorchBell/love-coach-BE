package com.torchbell.lovecoach.achievement.model;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
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
public class Achievement {
    private Long achievementId;
    private String title;
    private String description;
    private AchievementType achievementType;
    private int achievementValue;
    private String iconUrl;
    private Long rewardGalleryId;
}
