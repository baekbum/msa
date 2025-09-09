package com.example.user_service.dto;

import com.example.user_service.jpa.User;
import com.example.user_service.vo.InsertUser;
import com.example.user_service.vo.ResponseOrder;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private String userId;
    private String name;
    private String password;
    private String email;
    private Long teamId;
    private String teamName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.teamId = user.getTeamId();
        this.status = user.getStatus().name();
        this.createdAt = user.getCreateAt();
        this.updatedAt = user.getUpdatedAt();
    }
}