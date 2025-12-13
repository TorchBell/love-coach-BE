package com.torchbell.lovecoach.food.dto.response;

import com.torchbell.lovecoach.food.model.Food;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodResponse {
    private String foodId;
    private String foodName;
    private BigDecimal calory;
    private BigDecimal water;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carb;
    private BigDecimal sugar;
    private BigDecimal weight;

    public static FoodResponse from(Food food) {
        return FoodResponse.builder()
                .foodId(food.getFoodId())
                .foodName(food.getFoodName())
                .calory(food.getCalory())
                .water(food.getWater())
                .protein(food.getProtein())
                .fat(food.getFat())
                .carb(food.getCarb())
                .sugar(food.getSugar())
                .weight(food.getWeight())
                .build();
    }
}
