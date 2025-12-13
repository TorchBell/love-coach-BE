package com.torchbell.lovecoach.muscle.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MuscleExercise {
    private Long muscleExerciseId;
    private String name;
    private String part;
}
