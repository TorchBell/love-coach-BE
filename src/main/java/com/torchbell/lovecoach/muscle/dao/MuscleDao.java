package com.torchbell.lovecoach.muscle.dao;

import com.torchbell.lovecoach.muscle.model.MuscleExercise;
import com.torchbell.lovecoach.muscle.model.MuscleLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MuscleDao {
    // 근력 운동 종목 전체 조회
    List<MuscleExercise> selectMuscleExerciseList();

    // 근력 운동 종목 상세 조회
    Optional<MuscleExercise> selectMuscleExerciseById(@Param("muscleExerciseId") Long muscleExerciseId);

    // 유저 근력 운동 기록 조회 (월별)
    List<MuscleLog> selectMuscleLogList(@Param("userId") Long userId, @Param("year") int year,
            @Param("month") int month);

    // 유저 근력 운동 기록 상세 조회
    Optional<MuscleLog> selectMuscleLogById(@Param("muscleLogId") Long muscleLogId);

    // 근력 운동 기록 등록
    int insertMuscleLog(MuscleLog muscleLog);

    // 근력 운동 기록 수정
    int updateMuscleLog(MuscleLog muscleLog);

    // 근력 운동 기록 삭제
    int deleteMuscleLog(@Param("muscleLogId") Long muscleLogId);
}
