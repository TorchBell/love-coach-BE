package com.torchbell.lovecoach.cardio.model;

import com.torchbell.lovecoach.common.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class CardioLog extends BaseEntity {
    private Long cardioLogId;
    private Long userId;
    private Long cardioExerciseId;
    private LocalDate date;
    private Integer durationMinutes;
    private BigDecimal burnedKcal;

    // Join용 필드
    private String exerciseName;
    private BigDecimal metValue;
}
