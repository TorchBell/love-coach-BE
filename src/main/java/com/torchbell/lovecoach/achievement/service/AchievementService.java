package com.torchbell.lovecoach.achievement.service;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
import com.torchbell.lovecoach.achievement.dao.AchievementDao;
import com.torchbell.lovecoach.achievement.dto.response.AchievementResponse;
import com.torchbell.lovecoach.achievement.model.Achievement;
import com.torchbell.lovecoach.achievement.model.UserAchievement;
import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import com.torchbell.lovecoach.gallery.dao.GalleryDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementDao achievementDao;

    // 업적 목록 조회
    @Transactional(readOnly = true)
    public List<AchievementResponse> getAchievementList(Long userId) {
        return achievementDao.selectAchievementList(userId);
    }

    // 업적 상세 조회
    @Transactional(readOnly = true)
    public AchievementResponse getAchievementDetail(Long userId, Long achievementId) {
        Achievement achievement = achievementDao.selectAchievementById(achievementId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "업적을 찾을 수 없습니다."));

        Optional<UserAchievement> userAchievement = achievementDao.selectUserAchievement(userId, achievementId);

        return AchievementResponse.builder()
                .achievementId(achievement.getAchievementId())
                .title(achievement.getTitle())
                .description(achievement.getDescription())
                .achievementType(achievement.getAchievementType())
                .achievementValue(achievement.getAchievementValue())
                .iconUrl(achievement.getIconUrl())
                .rewardGalleryId(achievement.getRewardGalleryId())
                .isAchieved(userAchievement.isPresent())
                .achievedAt(userAchievement.map(UserAchievement::getAchievedAt).orElse(null))
                .build();
    }

    // 현재 업적 진행도 조회
    @Transactional(readOnly = true)
    public int getCurrentProgress(Long userId, AchievementType type) {
        List<AchievementResponse> allAchievements = achievementDao.selectAchievementList(userId);
        return allAchievements.stream()
                .filter(a -> type.equals(a.getAchievementType()))
                .mapToInt(AchievementResponse::getCurrentValue)
                .max()
                .orElse(0);
    }

}
