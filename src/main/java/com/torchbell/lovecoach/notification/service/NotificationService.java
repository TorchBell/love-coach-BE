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
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; // 1 hour

    private final Map<Long, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterMap.put(userId, emitter);

        // Completion & Timeout handling
        emitter.onCompletion(() -> emitterMap.remove(userId));
        emitter.onTimeout(() -> emitterMap.remove(userId));

        // Send dummy event to prevent Timeout on initial connection
        sendToClient(emitter, userId, "connect", "Connected to Notification Service");

        return emitter;
    }

    public void send(Long userId, String name, Object data) {
        SseEmitter emitter = emitterMap.get(userId);
        if (emitter != null) {
            sendToClient(emitter, userId, name, data);
        }
    }

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
