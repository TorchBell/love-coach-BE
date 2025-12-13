package com.torchbell.lovecoach.muscle.dto.request;

import com.torchbell.lovecoach.muscle.model.MuscleLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MuscleLogRequest {
    private Long muscleExerciseId;
    private LocalDate date;
    private Integer setCount;
    private Integer repsPerSet;
    private BigDecimal weight;

    public MuscleLog toEntity(Long userId) {
        return MuscleLog.builder()
                .userId(userId)
                .muscleExerciseId(muscleExerciseId)
                .date(date)
                .setCount(setCount)
                .repsPerSet(repsPerSet)
                .weight(weight)
                .build();
    }
}
