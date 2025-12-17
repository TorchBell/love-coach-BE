package com.torchbell.lovecoach.achievement.handler;

public interface AchievementHandler<T> {
    // 핸들러가 처리가능한 이벤트인지 확인하는 메서드
    boolean supports(Object event);

    // 실제 업적 로직 수행
    void handle(T event);

}
