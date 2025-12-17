package com.torchbell.lovecoach.achievement.event;

import com.torchbell.lovecoach.achievement.handler.AchievementHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AchievementEventListener {

    private final List<AchievementHandler<?>> handlers;

    @EventListener
    public void handleDomainEvent(Object event) {
        for (AchievementHandler<?> handler : handlers) {
            handleSafely(handler, event);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void handleSafely(AchievementHandler<T> handler, Object event) {
        if (handler.supports(event)) {
            handler.handle((T) event);
        }
    }
}

