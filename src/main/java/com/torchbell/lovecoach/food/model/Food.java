package com.torchbell.lovecoach.food.model;

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
public class Food {
    private String foodId;
    private String foodName;
    private BigDecimal calory;
    private BigDecimal water;
    private BigDecimal protein;
    private BigDecimal fat;
    private BigDecimal carb;
    private BigDecimal sugar;
    private BigDecimal weight;
}
