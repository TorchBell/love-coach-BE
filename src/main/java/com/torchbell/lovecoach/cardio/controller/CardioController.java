package com.torchbell.lovecoach.cardio.controller;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.cardio.dto.request.CardioLogRequest;
import com.torchbell.lovecoach.cardio.dto.response.CardioExerciseResponse;
import com.torchbell.lovecoach.cardio.dto.response.CardioLogResponse;
import com.torchbell.lovecoach.cardio.service.CardioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cardio-exercise")
@RequiredArgsConstructor
@Tag(name = "CardioController")
public class CardioController {

    private final CardioService cardioService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    // 운동 종목 목록 조회
    @GetMapping("/exercises")
    public ResponseEntity<List<CardioExerciseResponse>> getExerciseList() {
        return ResponseEntity.ok(cardioService.getExerciseList());
    }

    // 월별 운동 기록 조회
    @GetMapping
    public ResponseEntity<List<CardioLogResponse>> getMonthlyCardioLog(
            @RequestParam int year,
            @RequestParam int month,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(cardioService.getMonthlyCardioLog(userId, year, month));
    }

    // 운동 기록 상세 조회
    @GetMapping("/{cardioLogId}")
    public ResponseEntity<CardioLogResponse> getCardioLogDetail(@PathVariable Long cardioLogId) {
        return ResponseEntity.ok(cardioService.getCardioLogDetail(cardioLogId));
    }

    // 운동 기록 등록
    @PostMapping
    public ResponseEntity<Void> addCardioLog(
            @RequestBody CardioLogRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        cardioService.addCardioLog(userId, request);
        return ResponseEntity.ok().build();
    }

    // 운동 기록 수정
    @PutMapping("/{cardioLogId}")
    public ResponseEntity<Void> updateCardioLog(
            @PathVariable Long cardioLogId,
            @RequestBody CardioLogRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        cardioService.updateCardioLog(userId, cardioLogId, request);
        return ResponseEntity.ok().build();
    }

    // 운동 기록 삭제
    @DeleteMapping("/{cardioLogId}")
    public ResponseEntity<Void> deleteCardioLog(
            @PathVariable Long cardioLogId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        cardioService.deleteCardioLog(userId, cardioLogId);
        return ResponseEntity.ok().build();
    }
}
