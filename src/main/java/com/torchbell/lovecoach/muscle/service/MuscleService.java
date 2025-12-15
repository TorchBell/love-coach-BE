package com.torchbell.lovecoach.muscle.service;

import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import com.torchbell.lovecoach.muscle.dao.MuscleDao;
import com.torchbell.lovecoach.muscle.dto.request.MuscleLogRequest;
import com.torchbell.lovecoach.muscle.dto.response.MuscleExerciseResponse;
import com.torchbell.lovecoach.muscle.dto.response.MuscleLogResponse;
import com.torchbell.lovecoach.muscle.model.MuscleExercise;
import com.torchbell.lovecoach.muscle.model.MuscleLog;
import com.torchbell.lovecoach.npc.service.NpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MuscleService {
    private final MuscleDao muscleDao;
    private final NpcService npcService;
    // 운동 종목 목록 조회
    @Transactional(readOnly = true)
    public List<MuscleExerciseResponse> getExerciseList() {
        List<MuscleExercise> exercises = muscleDao.selectMuscleExerciseList();
        return exercises.stream()
                .map(MuscleExerciseResponse::from)
                .collect(Collectors.toList());
    }

    // 월별 운동 기록 조회
    @Transactional(readOnly = true)
    public List<MuscleLogResponse> getMonthlyMuscleLog(Long userId, int year, int month) {
        List<MuscleLog> logs = muscleDao.selectMuscleLogList(userId, year, month);
        return logs.stream()
                .map(MuscleLogResponse::from)
                .collect(Collectors.toList());
    }

    // 운동 기록 상세 조회
    @Transactional(readOnly = true)
    public MuscleLogResponse getMuscleLogDetail(Long muscleLogId) {
        MuscleLog log = muscleDao.selectMuscleLogById(muscleLogId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "운동 기록을 찾을 수 없습니다."));
        return MuscleLogResponse.from(log);
    }

    // 운동 기록 등록
    @Transactional
    public void addMuscleLog(Long userId, MuscleLogRequest request) {
        muscleDao.selectMuscleExerciseById(request.getMuscleExerciseId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 운동 종목입니다."));

        MuscleLog log = request.toEntity(userId);
        muscleDao.insertMuscleLog(log);
        npcService.increaseAffinity(userId, 2L, 1);
    }

    // 운동 기록 수정
    @Transactional
    public void updateMuscleLog(Long userId, Long muscleLogId, MuscleLogRequest request) {
        MuscleLog log = muscleDao.selectMuscleLogById(muscleLogId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "운동 기록을 찾을 수 없습니다."));

        if (!log.getUserId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "권한이 없습니다.");
        }

        muscleDao.selectMuscleExerciseById(request.getMuscleExerciseId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 운동 종목입니다."));

        MuscleLog updateLog = request.toEntity(userId);
        updateLog.setMuscleLogId(muscleLogId);

        muscleDao.updateMuscleLog(updateLog);
    }

    // 운동 기록 삭제
    @Transactional
    public void deleteMuscleLog(Long userId, Long muscleLogId) {
        MuscleLog log = muscleDao.selectMuscleLogById(muscleLogId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "운동 기록을 찾을 수 없습니다."));

        if (!log.getUserId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "권한이 없습니다.");
        }

        muscleDao.deleteMuscleLog(muscleLogId);
    }
}
