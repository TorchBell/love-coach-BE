package com.torchbell.lovecoach.achievement.dao;

import com.torchbell.lovecoach.achievement.dto.response.AchievementResponse;
import com.torchbell.lovecoach.achievement.model.Achievement;
import com.torchbell.lovecoach.achievement.model.UserAchievement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AchievementDao {
    // 업적 목록 조회 (유저 달성 여부 포함)
    List<AchievementResponse> selectAchievementList(@Param("userId") Long userId);

    // 업적 상세 조회
    Optional<Achievement> selectAchievementById(@Param("achievementId") Long achievementId);

    // 유저 업적 달성 여부 조회
    Optional<UserAchievement> selectUserAchievement(@Param("userId") Long userId,
            @Param("achievementId") Long achievementId);

    // 유저 업적 달성 기록
    int insertUserAchievement(UserAchievement userAchievement);

    int updateUserAchievement(UserAchievement userAchievement);
}
