package com.torchbell.lovecoach.muscle.model;

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
public class MuscleLog extends BaseEntity {
    private Long muscleLogId;
    private Long userId;
    private Long muscleExerciseId;
    private LocalDate date;
    private Integer setCount;
    private Integer repsPerSet;
    private BigDecimal weight;

    // Join용 필드
    private String exerciseName;
    private String exercisePart;
}
