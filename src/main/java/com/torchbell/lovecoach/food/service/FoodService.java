package com.torchbell.lovecoach.food.service;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
import com.torchbell.lovecoach.achievement.event.AchievementEvent;
import com.torchbell.lovecoach.achievement.service.AchievementService;
import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import com.torchbell.lovecoach.food.dao.FoodDao;
import com.torchbell.lovecoach.food.dto.request.UserFoodRequest;
import com.torchbell.lovecoach.food.dto.response.FoodResponse;
import com.torchbell.lovecoach.food.dto.response.UserFoodResponse;
import com.torchbell.lovecoach.food.model.Food;
import com.torchbell.lovecoach.food.model.UserFood;
import com.torchbell.lovecoach.npc.service.NpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodService {
    private final FoodDao foodDao;
    private final NpcService npcService;
    private final AchievementService achievementService;
    private final ApplicationEventPublisher eventPublisher;

    // 음식 검색
    @Transactional(readOnly = true)
    public List<FoodResponse> searchFood(String keyword) {
        List<Food> foods = foodDao.selectFoodList(keyword);
        return foods.stream()
                .map(FoodResponse::from)
                .collect(Collectors.toList());
    }

    // 월별 식단 조회
    @Transactional(readOnly = true)
    public List<UserFoodResponse> getMonthlyFoodLog(Long userId, int year, int month) {
        List<UserFood> userFoods = foodDao.selectUserFoodList(userId, year, month);
        return userFoods.stream()
                .map(UserFoodResponse::from)
                .collect(Collectors.toList());
    }

    // 식단 상세 조회
    @Transactional(readOnly = true)
    public UserFoodResponse getFoodLogDetail(Long userFoodId) {
        UserFood userFood = foodDao.selectUserFoodById(userFoodId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "식단 기록을 찾을 수 없습니다."));
        return UserFoodResponse.from(userFood);
    }

    // 식단 기록 등록
    @Transactional
    public void addFoodLog(Long userId, UserFoodRequest request) {
        // 음식 존재 여부 확인
        foodDao.selectFoodById(request.getFoodId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 음식입니다."));

        // 연속 기록 계산을 위한 마지막 기록 날짜 조회 (저장 전 조회)
        LocalDate lastDate = foodDao.selectLastLogDate(userId);

        UserFood userFood = request.toEntity(userId);
        foodDao.insertUserFood(userFood);

        // 연속 식단 기록 로직
        int currentStreak = achievementService.getCurrentProgress(userId,
                AchievementType.FOOD_STREAK);
        int newStreak = 1; // 기본값 (끊김 or 첫 시작)

        if (lastDate != null) {
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);

            if (lastDate.equals(today)) {
                // 오늘 이미 기록함 -> 유지
                newStreak = currentStreak;
            } else if (lastDate.equals(yesterday)) {
                // 어제 기록함 -> 연속 성공
                newStreak = currentStreak + 1;
            }
            // else: 어제보다 이전 -> 1 (초기화)
        }

        // 업적 이벤트 발행
        eventPublisher.publishEvent(new AchievementEvent(
                userId,
                AchievementType.FOOD_STREAK,
                newStreak));

        // NPC 1 (토마) 호감도 증가
        npcService.increaseAffinity(userId, 1L, 1);
    }

    // 식단 기록 수정
    @Transactional
    public void updateFoodLog(Long userId, Long userFoodId, UserFoodRequest request) {
        UserFood userFood = foodDao.selectUserFoodById(userFoodId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "식단 기록을 찾을 수 없습니다."));

        if (!userFood.getUserId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "권한이 없습니다.");
        }

        // 음식 존재 여부 확인
        foodDao.selectFoodById(request.getFoodId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 음식입니다."));

        UserFood updateUserFood = request.toEntity(userId);
        updateUserFood.setUserFoodId(userFoodId);

        foodDao.updateUserFood(updateUserFood);
    }

    // 식단 기록 삭제
    @Transactional
    public void deleteFoodLog(Long userId, Long userFoodId) {
        UserFood userFood = foodDao.selectUserFoodById(userFoodId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "식단 기록을 찾을 수 없습니다."));

        if (!userFood.getUserId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "권한이 없습니다.");
        }

        foodDao.deleteUserFood(userFoodId);
    }
}
