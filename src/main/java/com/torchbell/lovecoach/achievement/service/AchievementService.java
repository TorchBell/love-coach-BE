package com.torchbell.lovecoach.achievement.service;

import com.torchbell.lovecoach.achievement.constant.AchievementType;
import com.torchbell.lovecoach.achievement.dao.AchievementDao;
import com.torchbell.lovecoach.achievement.dto.response.AchievementResponse;
import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

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
        return achievementDao.selectAchievementDetail(userId, achievementId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "업적을 찾을 수 없습니다."));
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
