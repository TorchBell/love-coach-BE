package com.torchbell.lovecoach.quest.controller;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.quest.dto.response.QuestResponse;
import com.torchbell.lovecoach.quest.service.QuestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quest")
@RequiredArgsConstructor
@Tag(name = "QuestController")
public class QuestController {

    private final QuestService questService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    // 퀘스트 목록 조회
    @GetMapping
    public ResponseEntity<List<QuestResponse>> getQuestList(HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(questService.getQuestList(userId));
    }

    // 퀘스트 상세 조회
    @GetMapping("/{questId}")
    public ResponseEntity<QuestResponse> getQuestDetail(
            @PathVariable Long questId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(questService.getQuestDetail(userId, questId));
    }

    // 퀘스트 수락
    @PostMapping("/{questId}/accept")
    public ResponseEntity<Map<String, Object>> acceptQuest(
            @PathVariable Long questId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(questService.acceptQuest(userId, questId));
    }

    // 퀘스트 완료
    @PatchMapping("/{questId}/complete")
    public ResponseEntity<Map<String, Object>> completeQuest(
            @PathVariable Long questId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(questService.completeQuest(userId, questId));
    }
}
