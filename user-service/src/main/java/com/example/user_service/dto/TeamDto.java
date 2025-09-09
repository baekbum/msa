package com.example.user_service.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TeamDto {
    private Long id;
    private String name;
    private Long upperTeamId;
    private String upperTeamName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}