package com.torchbell.lovecoach.cardio.dto.request;

import com.torchbell.lovecoach.cardio.model.CardioLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardioLogRequest {
    private Long cardioExerciseId;
    private LocalDate date;
    private Integer durationMinutes;
    private BigDecimal burnedKcal; // 클라이언트가 계산해서 줄 수도 있고, 서버가 계산할 수도 있음. 일단 받음.

    public CardioLog toEntity(Long userId) {
        return CardioLog.builder()
                .userId(userId)
                .cardioExerciseId(cardioExerciseId)
                .date(date)
                .durationMinutes(durationMinutes)
                .burnedKcal(burnedKcal)
                .build();
    }
}
