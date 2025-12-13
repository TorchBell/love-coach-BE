package com.torchbell.lovecoach.muscle.controller;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.muscle.dto.request.MuscleLogRequest;
import com.torchbell.lovecoach.muscle.dto.response.MuscleExerciseResponse;
import com.torchbell.lovecoach.muscle.dto.response.MuscleLogResponse;
import com.torchbell.lovecoach.muscle.service.MuscleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/muscle-exercise")
@RequiredArgsConstructor
@Tag(name = "MuscleController")
public class MuscleController {

    private final MuscleService muscleService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    // 운동 종목 목록 조회 (추가)
    @GetMapping("/exercises")
    public ResponseEntity<List<MuscleExerciseResponse>> getExerciseList() {
        return ResponseEntity.ok(muscleService.getExerciseList());
    }

    // 월별 운동 기록 조회
    @GetMapping
    public ResponseEntity<List<MuscleLogResponse>> getMonthlyMuscleLog(
            @RequestParam int year,
            @RequestParam int month,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(muscleService.getMonthlyMuscleLog(userId, year, month));
    }

    // 운동 기록 상세 조회
    @GetMapping("/{muscleLogId}")
    public ResponseEntity<MuscleLogResponse> getMuscleLogDetail(@PathVariable Long muscleLogId) {
        return ResponseEntity.ok(muscleService.getMuscleLogDetail(muscleLogId));
    }

    // 운동 기록 등록
    @PostMapping
    public ResponseEntity<Void> addMuscleLog(
            @RequestBody MuscleLogRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        muscleService.addMuscleLog(userId, request);
        return ResponseEntity.ok().build();
    }

    // 운동 기록 수정
    @PutMapping("/{muscleLogId}")
    public ResponseEntity<Void> updateMuscleLog(
            @PathVariable Long muscleLogId,
            @RequestBody MuscleLogRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        muscleService.updateMuscleLog(userId, muscleLogId, request);
        return ResponseEntity.ok().build();
    }

    // 운동 기록 삭제
    @DeleteMapping("/{muscleLogId}")
    public ResponseEntity<Void> deleteMuscleLog(
            @PathVariable Long muscleLogId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        muscleService.deleteMuscleLog(userId, muscleLogId);
        return ResponseEntity.ok().build();
    }
}
