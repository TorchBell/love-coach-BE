package com.torchbell.lovecoach.achievement.event;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AchievementEvent {
    private Long userId;
    private AchievementType type;
    private int value; // 누적값 (예: 총 운동 횟수) 또는 갱신값 (현재 호감도)
}

