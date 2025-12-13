package com.torchbell.lovecoach.food.controller;

import com.torchbell.lovecoach.common.constant.WebSessionKey;
import com.torchbell.lovecoach.food.dto.request.UserFoodRequest;
import com.torchbell.lovecoach.food.dto.response.FoodResponse;
import com.torchbell.lovecoach.food.dto.response.UserFoodResponse;
import com.torchbell.lovecoach.food.service.FoodService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
@Tag(name = "FoodController")
public class FoodController {

    private final FoodService foodService;
    private final String USER_ID_KEY = WebSessionKey.LOGIN_USER_ID.getKey();

    // 음식 검색
    @GetMapping("/search")
    public ResponseEntity<List<FoodResponse>> searchFood(@RequestParam String keyword) {
        return ResponseEntity.ok(foodService.searchFood(keyword));
    }

    // 월별 식단 조회
    @GetMapping
    public ResponseEntity<List<UserFoodResponse>> getMonthlyFoodLog(
            @RequestParam int year,
            @RequestParam int month,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        return ResponseEntity.ok(foodService.getMonthlyFoodLog(userId, year, month));
    }

    // 식단 상세 조회
    @GetMapping("/{userFoodId}")
    public ResponseEntity<UserFoodResponse> getFoodLogDetail(@PathVariable Long userFoodId) {
        return ResponseEntity.ok(foodService.getFoodLogDetail(userFoodId));
    }

    // 식단 기록 등록
    @PostMapping
    public ResponseEntity<Void> addFoodLog(
            @RequestBody UserFoodRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        foodService.addFoodLog(userId, request);
        return ResponseEntity.ok().build();
    }

    // 식단 기록 수정
    @PutMapping("/{userFoodId}")
    public ResponseEntity<Void> updateFoodLog(
            @PathVariable Long userFoodId,
            @RequestBody UserFoodRequest request,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        foodService.updateFoodLog(userId, userFoodId, request);
        return ResponseEntity.ok().build();
    }

    // 식단 기록 삭제
    @DeleteMapping("/{userFoodId}")
    public ResponseEntity<Void> deleteFoodLog(
            @PathVariable Long userFoodId,
            HttpSession session) {
        Long userId = (Long) session.getAttribute(USER_ID_KEY);
        foodService.deleteFoodLog(userId, userFoodId);
        return ResponseEntity.ok().build();
    }
}
