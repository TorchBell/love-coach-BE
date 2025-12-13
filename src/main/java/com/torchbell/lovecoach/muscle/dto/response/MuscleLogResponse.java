package com.torchbell.lovecoach.muscle.dto.response;

import com.torchbell.lovecoach.muscle.model.MuscleLog;
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
public class MuscleLogResponse {
    private Long muscleLogId;
    private Long muscleExerciseId;
    private String exerciseName;
    private String exercisePart;
    private LocalDate date;
    private Integer setCount;
    private Integer repsPerSet;
    private BigDecimal weight;
    private BigDecimal totalVolume; // 볼륨 계산 (set * reps * weight)

    public static MuscleLogResponse from(MuscleLog log) {
        BigDecimal volume = BigDecimal.ZERO;
        if (log.getSetCount() != null && log.getRepsPerSet() != null && log.getWeight() != null) {
            volume = log.getWeight().multiply(BigDecimal.valueOf(log.getSetCount()))
                    .multiply(BigDecimal.valueOf(log.getRepsPerSet()));
        }

        return MuscleLogResponse.builder()
                .muscleLogId(log.getMuscleLogId())
                .muscleExerciseId(log.getMuscleExerciseId())
                .exerciseName(log.getExerciseName())
                .exercisePart(log.getExercisePart())
                .date(log.getDate())
                .setCount(log.getSetCount())
                .repsPerSet(log.getRepsPerSet())
                .weight(log.getWeight())
                .totalVolume(volume)
                .build();
    }
}
