package com.torchbell.lovecoach.cardio.dto.response;

import com.torchbell.lovecoach.cardio.model.CardioExercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardioExerciseResponse {
    private Long cardioExerciseId;
    private String name;
    private BigDecimal metValue;

    public static CardioExerciseResponse from(CardioExercise exercise) {
        return CardioExerciseResponse.builder()
                .cardioExerciseId(exercise.getCardioExerciseId())
                .name(exercise.getName())
                .metValue(exercise.getMetValue())
                .build();
    }
}
