package com.example.user_service.dto;

import com.example.user_service.jpa.UserEntity;
import com.example.user_service.vo.RequestUser;
import lombok.Data;

import java.util.Date;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;

    public UserDto(RequestUser user) {
        this.email = user.getEmail();
        this.pwd = user.getPwd();
        this.name = user.getName();
    }

    public UserDto(UserEntity entity) {
        this.email = entity.getEmail();
        this.pwd = entity.getEncryptedPwd();
        this.name = entity.getName();
        this.userId = entity.getUserId();
    }
}