package com.torchbell.lovecoach.user.dto.request;

import com.torchbell.lovecoach.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    private String email;
    private String password;

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
