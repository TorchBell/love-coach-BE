package com.torchbell.lovecoach.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
@Slf4j
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1 시간

    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    // 클라이언트가 알림을 구독할때 호출하는 메서드
    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterMap.put(userId, emitter);

        // 연결이 정상종료되었을 때 맵에서 해당 사용자 제거
        emitter.onCompletion(() -> emitterMap.remove(userId));

        // 연결 시간초과시에 맵에서 해당 사용자 제거
        emitter.onTimeout(() -> emitterMap.remove(userId));

        // 연결직후에 더미 이벤트를 전송해서 연결 성공 알려줌
        sendToClient(emitter, userId, "connect", "Connected to Notification Service");

        return emitter;
    }

   // 이벤트 프로세서에서 알림 보내고 싶을 때 이 공개메서드를 사용함
    public void send(Long userId, String name, Object data) {
        SseEmitter emitter = emitterMap.get(userId);
        if (emitter != null) {
            sendToClient(emitter, userId, name, data);
        }
    }

    // 실제 전송하는 로직
    private void sendToClient(SseEmitter emitter, Long userId, String name, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(String.valueOf(userId))
                    .name(name)
                    .data(data));
        } catch (IOException e) {
            emitterMap.remove(userId);
            log.error("Error sending SSE event to user {}", userId, e);
        }
    }
}
