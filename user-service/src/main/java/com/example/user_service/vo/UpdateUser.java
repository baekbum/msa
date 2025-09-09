package com.example.user_service.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUser {

    @Size(min = 2, message = "이름은 두 글자 이상입니다.")
    private String name;

    @Email
    private String email;

    @Size(min = 8, message = "비밀번호는 최소 여덟 글자 이상입니다.")
    private String password;

    private Long teamId;

    private String status;
}