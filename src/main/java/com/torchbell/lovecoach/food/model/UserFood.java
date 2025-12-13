package com.torchbell.lovecoach.food.model;

import com.torchbell.lovecoach.common.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class UserFood extends BaseEntity {
    private Long userFoodId;
    private Long userId;
    private String foodId;
    private LocalDate date;
    private BigDecimal quantity;

    // Join용 필드 (필요시 사용)
    private String foodName;
    private BigDecimal calory;
}
