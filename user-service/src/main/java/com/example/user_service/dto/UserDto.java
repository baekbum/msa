package com.example.user_service.dto;

import com.example.user_service.jpa.User;
import com.example.user_service.vo.RequestUser;
import com.example.user_service.vo.ResponseOrder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private Date createdAt;

    private String encryptedPwd;
    private List<ResponseOrder> orders;

    public UserDto(RequestUser user) {
        this.email = user.getEmail();
        this.pwd = user.getPassword();
        this.name = user.getName();
    }

    public UserDto(User entity) {
        this.email = entity.getEmail();
        this.pwd = entity.getEncryptedPwd();
        this.name = entity.getName();
        this.userId = entity.getUserId();
    }
}