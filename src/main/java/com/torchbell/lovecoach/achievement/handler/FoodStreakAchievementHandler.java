package com.torchbell.lovecoach.achievement.handler;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
import com.torchbell.lovecoach.achievement.event.AchievementEventProcessor;
import com.torchbell.lovecoach.achievement.service.AchievementService;
import com.torchbell.lovecoach.food.dao.FoodDao;
import com.torchbell.lovecoach.food.event.FoodLogCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FoodStreakAchievementHandler implements AchievementHandler<FoodLogCreatedEvent>{

    private final AchievementService achievementService;
    private final AchievementEventProcessor processor;
    private final FoodDao foodDao;

    @Override
    public boolean supports(Object event) {
        return event instanceof FoodLogCreatedEvent;
    }

    @Override
    public void handle(FoodLogCreatedEvent event) {
        Long userId = event.getUserId();

        // 1. 마지막 기록 날짜 조회
        LocalDate lastDate = foodDao.selectLastLogDate(userId);

        // 2. 현재 연속 기록 조회
        int currentStreak = achievementService.getCurrentProgress(userId, AchievementType.FOOD_STREAK);
        int newStreak = 1;

        // 3. 연속 기록 계산 로직
        if (lastDate != null) {
            LocalDate today = event.getDate();
            LocalDate yesterday = today.minusDays(1);

            if (lastDate.equals(today)) {
                newStreak = currentStreak; // 오늘 이미 기록함 -> 유지
            } else if (lastDate.equals(yesterday)) {
                newStreak = currentStreak + 1; // 어제 기록함 -> 연속 성공
            }
            // else: 끊김 -> 1
        }

        // 4. 프로세서에게 처리 위임
        processor.checkAchievementProgress(userId, AchievementType.FOOD_STREAK, newStreak);
    }
}
