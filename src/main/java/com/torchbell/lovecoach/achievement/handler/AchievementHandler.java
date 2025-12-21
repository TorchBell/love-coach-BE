package com.torchbell.lovecoach.achievement.handler;

public interface AchievementHandler<T> {

    // 핸들러가 처리가능한 이벤트인지 확인하는 메서드
    boolean supports(Object event);

    // 실제 업적 로직 수행
    // 이 메서드에서는 판단을 하지는 않고 판단에 필요한 정보들을 만들어서 넘겨주는 역할임
    void handle(T event);

}
