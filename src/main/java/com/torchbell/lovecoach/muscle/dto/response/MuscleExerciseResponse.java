package com.torchbell.lovecoach.muscle.dto.response;

import com.torchbell.lovecoach.muscle.model.MuscleExercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuscleExerciseResponse {
    private Long muscleExerciseId;
    private String name;
    private String part;

    public static MuscleExerciseResponse from(MuscleExercise exercise) {
        return MuscleExerciseResponse.builder()
                .muscleExerciseId(exercise.getMuscleExerciseId())
                .name(exercise.getName())
                .part(exercise.getPart())
                .build();
    }
}
