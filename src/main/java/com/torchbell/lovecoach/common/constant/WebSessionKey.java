package com.torchbell.lovecoach.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebSessionKey {
    LOGIN_USER_ID("LOGIN_USER_ID");
    private final String key;
}