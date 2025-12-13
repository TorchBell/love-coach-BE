package com.torchbell.lovecoach.achievement.model;

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
    private String conditionType;
    private String iconUrl;
    private Long rewardGalleryId;
}
