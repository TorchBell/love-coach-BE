package com.torchbell.lovecoach.common.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BaseEntity {
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
