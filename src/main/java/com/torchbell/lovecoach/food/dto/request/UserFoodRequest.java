package com.torchbell.lovecoach.food.dto.request;

import com.torchbell.lovecoach.food.model.UserFood;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserFoodRequest {
    private String foodId;
    private LocalDate date;
    private BigDecimal quantity;

    public UserFood toEntity(Long userId) {
        return UserFood.builder()
                .userId(userId)
                .foodId(foodId)
                .date(date)
                .quantity(quantity)
                .build();
    }
}
