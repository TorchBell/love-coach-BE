package com.torchbell.lovecoach.user.dto.response;


import com.torchbell.lovecoach.user.model.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserInfoResponse {
    private Long userId;
    private String email;
    private String nickname;
    private String gender;
    private LocalDate birthDate;
    private Integer credit;

    public static UserInfoResponse from(User user) {
        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .birthDate(user.getBirthDate())
                .credit(user.getCredit())
                .build();
    }
}
