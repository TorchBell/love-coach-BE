package com.torchbell.lovecoach.npc.dao;

import com.torchbell.lovecoach.npc.dto.response.NpcInfoResponse;
import com.torchbell.lovecoach.npc.model.ChatLog;
import com.torchbell.lovecoach.npc.model.Npc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface NpcDao {

    // npc 생성
    int insertNpc(@Param("userId") Long userId);

    // npc목록 조회
    List<NpcInfoResponse> selectNpcListByUserId(@Param("userId") Long userId);

    // 단일 npc 조회
    Npc selectNpcById(@Param("npcId") Long npcId);

    // 채팅 로그 조회
    List<ChatLog> selectChatLogListByUserIdAndNpcId(
            @Param("userId") Long userId,
            @Param("npcId") Long npcId,
            @Param("offset") int offset,
            @Param("limit") int limit);

    // 채팅 저장
    int insertChatLog(ChatLog chatLog);


    // UserNpc 조회
    com.torchbell.lovecoach.npc.model.UserNpc selectUserNpc(@Param("userId") Long userId, @Param("npcId") Long npcId);

    // UserNpc 수정 (호감도 등)
    int updateUserNpc(com.torchbell.lovecoach.npc.model.UserNpc userNpc);

}
