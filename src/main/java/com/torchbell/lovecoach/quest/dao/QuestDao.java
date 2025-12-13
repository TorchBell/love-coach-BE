package com.torchbell.lovecoach.quest.dao;

import com.torchbell.lovecoach.quest.dto.response.QuestResponse;
import com.torchbell.lovecoach.quest.model.Quest;
import com.torchbell.lovecoach.quest.model.UserQuest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface QuestDao {
    // 퀘스트 목록 조회 (유저 상태 포함)
    List<QuestResponse> selectQuestList(@Param("userId") Long userId);

    // 퀘스트 상세 조회
    Optional<Quest> selectQuestById(@Param("questId") Long questId);

    // 유저 퀘스트 조회
    Optional<UserQuest> selectUserQuest(@Param("userId") Long userId, @Param("questId") Long questId);

    // 유저 퀘스트 생성 (수락)
    int insertUserQuest(UserQuest userQuest);

    // 유저 퀘스트 수정 (완료)
    int updateUserQuest(UserQuest userQuest);
}
