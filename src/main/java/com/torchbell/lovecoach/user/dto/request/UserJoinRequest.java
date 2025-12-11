package com.torchbell.lovecoach.user.dto.request;


import com.torchbell.lovecoach.user.model.User;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserJoinRequest {
    private String email;
    private String password;
    private String nickname;
    private String gender;
    private LocalDate birthDate;

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .gender(gender)
                .birthDate(birthDate)
                .build();
    }
}
