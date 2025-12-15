package com.torchbell.lovecoach.achievement.dto.response;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
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
    private AchievementType achievementType;
    private int achievementValue;
    private String iconUrl;
    private Long rewardGalleryId;
    private Boolean isAchieved;
    private LocalDateTime achievedAt;
    private int currentValue; // 현재 진행도
}
