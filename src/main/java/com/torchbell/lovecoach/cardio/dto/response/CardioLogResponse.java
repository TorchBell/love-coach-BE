package com.torchbell.lovecoach.cardio.dto.response;

import com.torchbell.lovecoach.cardio.model.CardioLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardioLogResponse {
    private Long cardioLogId;
    private Long cardioExerciseId;
    private String exerciseName;
    private LocalDate date;
    private Integer durationMinutes;
    private BigDecimal burnedKcal;

    public static CardioLogResponse from(CardioLog log) {
        return CardioLogResponse.builder()
                .cardioLogId(log.getCardioLogId())
                .cardioExerciseId(log.getCardioExerciseId())
                .exerciseName(log.getExerciseName())
                .date(log.getDate())
                .durationMinutes(log.getDurationMinutes())
                .burnedKcal(log.getBurnedKcal())
                .build();
    }
}
