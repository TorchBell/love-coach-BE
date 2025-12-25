package com.torchbell.lovecoach.food.dto.response;

import com.torchbell.lovecoach.food.model.UserFood;
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
public class UserFoodResponse {
    private Long userFoodId;
    private String foodId;
    private String foodName;
    private LocalDate date;
    private BigDecimal quantity;
    private BigDecimal calory; // 1인분 기준 * quantity? 아니면 1인분 기준? -> 보통 총 섭취 칼로리를 보여주는게 좋음.
    // 하지만 DB에는 100g당 혹은 1인분당 정보가 있고, quantity는 배수.
    // UserFood 모델에 join된 calory는 1인분(혹은 기준) 칼로리일 것.
    // 여기서는 계산된 값을 줄 수도 있고, 원본을 줄 수도 있음.
    // 일단 UserFood 모델의 값을 그대로 매핑.
    private BigDecimal water;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carb;
    private BigDecimal sugar;

    public static UserFoodResponse from(UserFood userFood) {
        return UserFoodResponse.builder()
                .userFoodId(userFood.getUserFoodId())
                .foodId(userFood.getFoodId())
                .foodName(userFood.getFoodName())
                .date(userFood.getDate())
                .quantity(userFood.getQuantity())
                .calory(userFood.getCalory()) // Join된 값
                .water(userFood.getCalory())
                .protein(userFood.getProtein())
                .fat(userFood.getFat())
                .carb(userFood.getCarb())
                .sugar(userFood.getSugar())
                .build();
    }
}
