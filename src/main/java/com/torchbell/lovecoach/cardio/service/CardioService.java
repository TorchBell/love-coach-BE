package com.torchbell.lovecoach.cardio.service;

import com.torchbell.lovecoach.common.exception.BusinessLogicException;
import com.torchbell.lovecoach.common.exception.ErrorCode;
import com.torchbell.lovecoach.cardio.dao.CardioDao;
import com.torchbell.lovecoach.cardio.dto.request.CardioLogRequest;
import com.torchbell.lovecoach.cardio.dto.response.CardioExerciseResponse;
import com.torchbell.lovecoach.cardio.dto.response.CardioLogResponse;
import com.torchbell.lovecoach.cardio.model.CardioExercise;
import com.torchbell.lovecoach.cardio.model.CardioLog;
import com.torchbell.lovecoach.npc.service.NpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardioService {
    private final CardioDao cardioDao;
    private final NpcService npcService;
    // 운동 종목 목록 조회
    @Transactional(readOnly = true)
    public List<CardioExerciseResponse> getExerciseList() {
        List<CardioExercise> exercises = cardioDao.selectCardioExerciseList();
        return exercises.stream()
                .map(CardioExerciseResponse::from)
                .collect(Collectors.toList());
    }

    // 월별 운동 기록 조회
    @Transactional(readOnly = true)
    public List<CardioLogResponse> getMonthlyCardioLog(Long userId, int year, int month) {
        List<CardioLog> logs = cardioDao.selectCardioLogList(userId, year, month);
        return logs.stream()
                .map(CardioLogResponse::from)
                .collect(Collectors.toList());
    }

    // 운동 기록 상세 조회
    @Transactional(readOnly = true)
    public CardioLogResponse getCardioLogDetail(Long cardioLogId) {
        CardioLog log = cardioDao.selectCardioLogById(cardioLogId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "운동 기록을 찾을 수 없습니다."));
        return CardioLogResponse.from(log);
    }

    // 운동 기록 등록
    @Transactional
    public void addCardioLog(Long userId, CardioLogRequest request) {
        cardioDao.selectCardioExerciseById(request.getCardioExerciseId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 운동 종목입니다."));

        CardioLog log = request.toEntity(userId);
        cardioDao.insertCardioLog(log);
        npcService.increaseAffinity(userId, 3L, 1);
    }

    // 운동 기록 수정
    @Transactional
    public void updateCardioLog(Long userId, Long cardioLogId, CardioLogRequest request) {
        CardioLog log = cardioDao.selectCardioLogById(cardioLogId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "운동 기록을 찾을 수 없습니다."));

        if (!log.getUserId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "권한이 없습니다.");
        }

        cardioDao.selectCardioExerciseById(request.getCardioExerciseId())
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "존재하지 않는 운동 종목입니다."));

        CardioLog updateLog = request.toEntity(userId);
        updateLog.setCardioLogId(cardioLogId);

        cardioDao.updateCardioLog(updateLog);
    }

    // 운동 기록 삭제
    @Transactional
    public void deleteCardioLog(Long userId, Long cardioLogId) {
        CardioLog log = cardioDao.selectCardioLogById(cardioLogId)
                .orElseThrow(() -> new BusinessLogicException(ErrorCode.RESOURCE_NOT_FOUND, "운동 기록을 찾을 수 없습니다."));

        if (!log.getUserId().equals(userId)) {
            throw new BusinessLogicException(ErrorCode.FORBIDDEN, "권한이 없습니다.");
        }

        cardioDao.deleteCardioLog(cardioLogId);
    }
}
