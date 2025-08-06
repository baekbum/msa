package com.example.user_service.dto;

import com.example.user_service.jpa.User;
import com.example.user_service.vo.RequestUser;
import com.example.user_service.vo.ResponseOrder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class UserDto {
    private String userId;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;

    private List<ResponseOrder> orders;

    public UserDto(RequestUser user) {
        this.userId = user.getUser_id();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
    }

    public UserDto(User entity) {
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.email = entity.getEmail();
        this.createdAt = entity.getCreateAt();
    }
}