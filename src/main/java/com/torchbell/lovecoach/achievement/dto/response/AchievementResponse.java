package com.torchbell.lovecoach.achievement.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AchievementResponse {
    private Long achievementId;
    private String title;
    private String description;
    private String conditionType;
    private String iconUrl;
    private Long rewardGalleryId;
    private Boolean isAchieved;
    private LocalDateTime achievedAt;
}
