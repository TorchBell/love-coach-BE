package com.torchbell.lovecoach.user.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreditUsageRequest {
    private Integer amount;
}
