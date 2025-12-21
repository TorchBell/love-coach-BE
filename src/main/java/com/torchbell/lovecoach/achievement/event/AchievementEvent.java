package com.torchbell.lovecoach.achievement.event;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 업적별로 핸들러를 따로 만들게되면서 이 코드는 사용하지 않음
public class AchievementEvent {
    private Long userId;
    private AchievementType type;
    private int value;
}

