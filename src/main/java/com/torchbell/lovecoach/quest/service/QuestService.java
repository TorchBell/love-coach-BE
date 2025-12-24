package com.torchbell.lovecoach.quest.service;

import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import com.torchbell.lovecoach.quest.dao.QuestDao;
import com.torchbell.lovecoach.quest.dto.response.QuestResponse;
import com.torchbell.lovecoach.quest.model.Quest;
import com.torchbell.lovecoach.quest.model.UserQuest;
import com.torchbell.lovecoach.user.dao.UserDao;
import com.torchbell.lovecoach.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final QuestDao questDao;
    private final UserDao userDao; // 크레딧 지급을 위해 필요

    // 퀘스트 목록 조회
    @Transactional(readOnly = true)
    public List<QuestResponse> getQuestList(Long userId) {
        return questDao.selectQuestList(userId);
    }

    // 퀘스트 상세 조회
    @Transactional(readOnly = true)
    public QuestResponse getQuestDetail(Long userId, Long questId) {
        Quest quest = questDao.selectQuestById(questId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "퀘스트를 찾을 수 없습니다."));

        Optional<UserQuest> userQuest = questDao.selectUserQuest(userId, questId);

        return QuestResponse.builder()
                .questId(quest.getQuestId())
                .title(quest.getTitle())
                .content(quest.getContent())
                .npcId(quest.getNpcId())
                .credit(quest.getCredit())
                .isAccepted(userQuest.isPresent())
                .isAchieved(userQuest.map(UserQuest::getIsAchieved).orElse(false))
                .build();
    }

    // 퀘스트 수락
    @Transactional
    public Map<String, Object> acceptQuest(Long userId, Long questId) {
        questDao.selectQuestById(questId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "퀘스트를 찾을 수 없습니다."));

        Optional<UserQuest> optionalUserQuest = questDao.selectUserQuest(userId, questId);
        if (optionalUserQuest.isPresent()) {
            throw new BusinessLogicException(ErrorCode.DUPLICATE_RESOURCE, "이미 수락한 퀘스트입니다.");
        }

        UserQuest userQuest = UserQuest.builder()
                .userId(userId)
                .questId(questId)
                .isAchieved(false)
                .build();
        questDao.insertUserQuest(userQuest);

        Map<String, Object> result = new HashMap<>();
        result.put("userQuestId", userQuest.getUserQuestId());
        result.put("questId", questId);
        result.put("status", "ACCEPTED");
        return result;
    }

    // 퀘스트 완료
    @Transactional
    public Map<String, Object> completeQuest(Long userId, Long questId) {
        Quest quest = questDao.selectQuestById(questId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "퀘스트를 찾을 수 없습니다."));

        UserQuest userQuest = questDao.selectUserQuest(userId, questId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.BAD_REQUEST, "수락하지 않은 퀘스트입니다."));

        if (userQuest.getIsAchieved()) {
            throw new BusinessLogicException(ErrorCode.BAD_REQUEST, "이미 완료한 퀘스트입니다.");
        }

        // 퀘스트 완료 처리
        userQuest.setIsAchieved(true);
        questDao.updateUserQuest(userQuest);

        // 크레딧 지급
        if (quest.getCredit() > 0) {
            // UserDao.updateCredit은 amount를 더하므로, 양수를 넣으면 증가함.
            userDao.updateCredit(userId, quest.getCredit());
        }

        // 현재 크레딧 조회
        User user = userDao.selectUserById(userId).orElseThrow();

        Map<String, Object> result = new HashMap<>();
        result.put("questId", questId);
        result.put("isAchieved", true);
        result.put("earnedCredit", quest.getCredit());
        result.put("totalCredit", user.getCredit());
        return result;
    }
}
