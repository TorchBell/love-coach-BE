package com.torchbell.lovecoach.achievement.controller;

import com.torchbell.lovecoach.achievement.dto.response.AchievementResponse;
import com.torchbell.lovecoach.achievement.service.AchievementService;
import com.torchbell.lovecoach.common.constant.WebSessionKey;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievement")
@RequiredArgsConstructor
@Tag(name = "AchievementController")
public class AchievementController {

    private final AchievementService achievementService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    // 업적 목록 조회
    @GetMapping
    public ResponseEntity<List<AchievementResponse>> getAchievementList(HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(achievementService.getAchievementList(userId));
    }

    // 업적 상세 조회
    @GetMapping("/{achievementId}")
    public ResponseEntity<AchievementResponse> getAchievementDetail(
            @PathVariable Long achievementId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(achievementService.getAchievementDetail(userId, achievementId));
    }

}
