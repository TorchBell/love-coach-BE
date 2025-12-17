package com.torchbell.lovecoach.food.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class FoodLogCreatedEvent {
    private Long userId;
    private LocalDate date;
}
