package com.torchbell.lovecoach.cardio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CardioExercise {
    private Long cardioExerciseId;
    private String name;
    private BigDecimal metValue;
}
