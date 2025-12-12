package com.torchbell.lovecoach.common.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
