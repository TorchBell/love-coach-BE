package com.torchbell.lovecoach.food.dao;

import com.torchbell.lovecoach.food.model.Food;
import com.torchbell.lovecoach.food.model.UserFood;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FoodDao {
    // 음식 검색
    List<Food> selectFoodList(@Param("keyword") String keyword);

    // 음식 상세 조회
    Optional<Food> selectFoodById(@Param("foodId") String foodId);

    // 유저 식단 기록 조회 (월별)
    List<UserFood> selectUserFoodList(@Param("userId") Long userId, @Param("year") int year, @Param("month") int month);

    // 유저 식단 기록 상세 조회
    Optional<UserFood> selectUserFoodById(@Param("userFoodId") Long userFoodId);

    // 식단 기록 등록
    int insertUserFood(UserFood userFood);

    // 식단 기록 수정
    int updateUserFood(UserFood userFood);

    // 식단 기록 삭제
    int deleteUserFood(@Param("userFoodId") Long userFoodId);
}
