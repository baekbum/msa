package com.example.user_service.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InsertUser {

    @NotNull(message = "유저 ID는 필수 값입니다.")
    private String id;

    @NotNull(message = "유저 이름은 필수 값입니다.")
    @Size(min = 2, message = "이름은 두 글자 이상입니다.")
    private String name;

    @NotNull(message = "이메일은 필수 값입니다.")
    @Email
    private String email;

    @NotNull(message = "비밀번호는 필수 값입니다.")
    @Size(min = 8, message = "비밀번호는 최소 여덟 글자 이상입니다.")
    private String password;

    @NotNull(message = "팀 ID는 필수 값입니다.")
    private Long teamId;
}