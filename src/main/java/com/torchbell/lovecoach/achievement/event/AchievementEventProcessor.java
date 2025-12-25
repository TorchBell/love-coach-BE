package com.torchbell.lovecoach.achievement.event;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
import com.torchbell.lovecoach.achievement.dao.AchievementDao;
import com.torchbell.lovecoach.achievement.dto.response.AchievementResponse;
import com.torchbell.lovecoach.achievement.model.UserAchievement;
import com.torchbell.lovecoach.gallery.dao.GalleryDao;
import com.torchbell.lovecoach.gallery.model.UserGallery;
import com.torchbell.lovecoach.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AchievementEventProcessor {
    private final AchievementDao achievementDao;
    private final GalleryDao galleryDao;
    private final NotificationService notificationService;

    // 업적 진행도 체크 및 달성 처리
    @Transactional
    public void checkAchievementProgress(Long userId, AchievementType type, int currentValue, Long npcId) {

        List<AchievementResponse> allAchievements = achievementDao.selectAchievementList(userId);
        // 1. 해당 타입의 모든 업적 조회
        List<AchievementResponse> targetAchievements = allAchievements.stream()
                .filter(a -> type.equals(a.getAchievementType()))
                .filter(a -> npcId == null || npcId.equals(a.getNpcId())) // npcId가 주어진 경우 해당 NPC의 업적만 필터링
                .toList();

        for (AchievementResponse ach : targetAchievements) {
            if (ach.getIsAchieved()) {
                continue; // 이미 달성함 -> 패스
            }

            // 2. 진행도 업데이트
            Optional<UserAchievement> optionalUserAchievement = achievementDao.selectUserAchievement(userId,
                    ach.getAchievementId());
            UserAchievement userAchievement;

            // 해당 user_achievement가 없는 경우에 만들어줌
            if (optionalUserAchievement.isEmpty()) {
                userAchievement = UserAchievement.builder()
                        .userId(userId)
                        .achievementId(ach.getAchievementId())
                        .currentValue(currentValue)
                        .build();
                achievementDao.insertUserAchievement(userAchievement);
            } else {
                userAchievement = optionalUserAchievement.get();
                userAchievement.setCurrentValue(currentValue);
                achievementDao.updateUserAchievement(userAchievement);
            }

            // 3. 달성 조건 확인
            // 현재 로직상으로는 증가하는 값에대한 업적의 경우만 취급하고 있음
            if (currentValue >= ach.getAchievementValue()) {
                if (userAchievement.getAchievedAt() == null) {
                    userAchievement.setAchievedAt(LocalDateTime.now());
                    achievementDao.updateUserAchievement(userAchievement);

                    // 보상 지급
                    unlockReward(userId, ach.getRewardGalleryId());

                    // 알림 전송
                    System.out.println("Notification Sent: Achievement Unlocked - " + ach.getTitle());
                    notificationService.send(userId, "achievement", ach);
                }
            }
        }
    }

    // 해당 코드는 업적 달성을 통해서 갤러리를 해금하는 경우에만 사용하는 메서드임
    // credit을 통해서 갤러리를 해금하는 경우에는 galleryService에서 바로 notificationService에 접근함
    private void unlockReward(Long userId, Long rewardGalleryId) {

        // 업적에 대해당하는 갤러리가 없는 경우에 아무런 동작도 수행하지 않음
        if (rewardGalleryId == null)
            return;

        Optional<UserGallery> userGallery = galleryDao.selectUserGallery(userId, rewardGalleryId);
        boolean isUnlocked = false;

        if (userGallery.isEmpty()) {
            UserGallery newUserGallery = UserGallery.builder()
                    .userId(userId)
                    .galleryId(rewardGalleryId)
                    .isOpened(true)
                    .isBOpened(false)
                    .build();
            galleryDao.insertUserGallery(newUserGallery);
            isUnlocked = true;
        } else {
            UserGallery existingGallery = userGallery.get();
            if (!existingGallery.getIsOpened()) {
                existingGallery.setIsOpened(true);
                galleryDao.updateUserGallery(existingGallery);
                isUnlocked = true;
            }
        }

        if (isUnlocked) {
            Map<String, Object> result = new HashMap<>();
            result.put("galleryId", rewardGalleryId);
            result.put("isOpened", true);
            result.put("message", "업적 보상으로 갤러리가 해금되었습니다.");

            System.out.println("Notification Sent: Gallery Unlocked - ID: " + rewardGalleryId);
            notificationService.send(userId, "gallery", result);
        }
    }

}
