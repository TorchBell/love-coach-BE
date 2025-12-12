package com.torchbell.lovecoach.user.model;

import com.torchbell.lovecoach.common.model.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {
    private Long userId;
    private String email;
    private String password;
    private String nickname;
    private String gender;
    private LocalDate birthDate;
    private Integer credit;
    private Boolean isActive;

}