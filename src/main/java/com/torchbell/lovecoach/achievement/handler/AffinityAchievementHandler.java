package com.torchbell.lovecoach.achievement.handler;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
import com.torchbell.lovecoach.achievement.event.AchievementEventProcessor;
import com.torchbell.lovecoach.npc.event.AffinityChangedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AffinityAchievementHandler implements AchievementHandler<AffinityChangedEvent> {

    private final AchievementEventProcessor processor;

    @Override
    public boolean supports(Object event) {
        return event instanceof AffinityChangedEvent;
    }

    @Override
    public void handle(AffinityChangedEvent event) {
        // 호감도 이벤트는 별도로 계산이 필요없음

        processor.checkAchievementProgress(
                event.getUserId(),
                AchievementType.AFFECTION,
                event.getNewScore(),
                event.getNpcId());
    }
}
