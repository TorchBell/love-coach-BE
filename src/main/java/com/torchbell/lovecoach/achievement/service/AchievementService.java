package com.torchbell.lovecoach.achievement.service;

import com.torchbell.lovecoach.achievement.dao.AchievementDao;
import com.torchbell.lovecoach.achievement.dto.response.AchievementResponse;
import com.torchbell.lovecoach.achievement.model.Achievement;
import com.torchbell.lovecoach.achievement.model.UserAchievement;
import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import com.torchbell.lovecoach.gallery.dao.GalleryDao;
import com.torchbell.lovecoach.gallery.model.UserGallery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AchievementService {
    private final AchievementDao achievementDao;
    private final GalleryDao galleryDao; // 보상 지급을 위해 필요

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
                .conditionType(achievement.getConditionType())
                .iconUrl(achievement.getIconUrl())
                .rewardGalleryId(achievement.getRewardGalleryId())
                .isAchieved(userAchievement.isPresent())
                .achievedAt(userAchievement.map(UserAchievement::getAchievedAt).orElse(null))
                .build();
    }

    // 업적 달성 처리
    @Transactional
    public Map<String, Object> achieveAchievement(Long userId, Long achievementId) {
        Achievement achievement = achievementDao.selectAchievementById(achievementId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "업적을 찾을 수 없습니다."));

        Optional<UserAchievement> optionalUserAchievement = achievementDao.selectUserAchievement(userId, achievementId);

        if (optionalUserAchievement.isPresent()) {
            Map<String, Object> result = new HashMap<>();
            result.put("achievementId", achievementId);
            result.put("isAchieved", true);
            result.put("message", "이미 달성한 업적입니다.");
            return result;
        }

        // 업적 달성 기록
        UserAchievement userAchievement = UserAchievement.builder()
                .userId(userId)
                .achievementId(achievementId)
                .achievedAt(LocalDateTime.now())
                .build();
        achievementDao.insertUserAchievement(userAchievement);

        // 보상 지급 (갤러리 해금)
        if (achievement.getRewardGalleryId() != null) {
            Optional<UserGallery> userGallery = galleryDao.selectUserGallery(userId, achievement.getRewardGalleryId());
            if (userGallery.isEmpty()) {
                UserGallery newUserGallery = UserGallery.builder()
                        .userId(userId)
                        .galleryId(achievement.getRewardGalleryId())
                        .isOpened(true)
                        .isFavorite(false)
                        .build();
                galleryDao.insertUserGallery(newUserGallery);
            } else {
                UserGallery existingGallery = userGallery.get();
                if (!existingGallery.getIsOpened()) {
                    existingGallery.setIsOpened(true);
                    galleryDao.updateUserGallery(existingGallery);
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("achievementId", achievementId);
        result.put("isAchieved", true);
        result.put("rewardGalleryId", achievement.getRewardGalleryId());
        result.put("message", "업적을 달성했습니다!");
        return result;
    }
}
