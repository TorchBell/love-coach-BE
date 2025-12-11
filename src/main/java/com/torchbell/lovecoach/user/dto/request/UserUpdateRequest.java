package com.torchbell.lovecoach.user.dto.request;

import com.torchbell.lovecoach.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {
    private String password;
    private String newPassword;
    private String nickname;
    private String gender;
    private LocalDate birthDate;

    public User toEntity(Long userId) {
        return User.builder()
                .userId(userId)
                .password(newPassword)
                .nickname(nickname)
                .gender(gender)
                .birthDate(birthDate)
                .build();
    }

}
