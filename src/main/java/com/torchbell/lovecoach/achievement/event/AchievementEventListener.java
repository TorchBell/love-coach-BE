package com.torchbell.lovecoach.achievement.event;

import com.torchbell.lovecoach.achievement.service.AchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AchievementEventListener {

    private final AchievementEventProcessor achievementEventProcessor;

    @EventListener
    public void handleAchievementEvent(AchievementEvent event) {
        System.out.println(
                "[AchievementEventListener] Received event: " + event.getType() + ", Value: " + event.getValue());
        achievementEventProcessor.checkAchievementProgress(event.getUserId(), event.getType(), event.getValue());
    }
}
