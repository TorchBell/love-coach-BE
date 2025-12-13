package com.torchbell.lovecoach.cardio.dao;

import com.torchbell.lovecoach.cardio.model.CardioExercise;
import com.torchbell.lovecoach.cardio.model.CardioLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CardioDao {
    // 유산소 운동 종목 전체 조회
    List<CardioExercise> selectCardioExerciseList();

    // 유산소 운동 종목 상세 조회
    Optional<CardioExercise> selectCardioExerciseById(@Param("cardioExerciseId") Long cardioExerciseId);

    // 유저 유산소 운동 기록 조회 (월별)
    List<CardioLog> selectCardioLogList(@Param("userId") Long userId, @Param("year") int year,
            @Param("month") int month);

    // 유저 유산소 운동 기록 상세 조회
    Optional<CardioLog> selectCardioLogById(@Param("cardioLogId") Long cardioLogId);

    // 유산소 운동 기록 등록
    int insertCardioLog(CardioLog cardioLog);

    // 유산소 운동 기록 수정
    int updateCardioLog(CardioLog cardioLog);

    // 유산소 운동 기록 삭제
    int deleteCardioLog(@Param("cardioLogId") Long cardioLogId);
}
