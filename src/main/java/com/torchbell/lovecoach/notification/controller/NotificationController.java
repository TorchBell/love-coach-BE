package com.torchbell.lovecoach.notification.controller;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "NotificationController")
public class NotificationController {
    private final NotificationService notificationService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        // AuthInterceptor에서 로그인 체크를 수행하므로, 여기서는 userId가 null이 아님
        return notificationService.subscribe(userId);
    }

    @Operation(summary = "테스트 알림 전송", description = "특정 유저에게 테스트 알림을 보냅니다.")
    @PostMapping("/send-test/{userId}")
    public ResponseEntity<String> sendTestNotification(@PathVariable Long userId) {
        notificationService.send(userId, "notification", "테스트 알림입니다!");
        return ResponseEntity.ok("알림 전송 완료");
    }

}
