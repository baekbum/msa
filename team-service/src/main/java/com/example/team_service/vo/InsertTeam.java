package com.example.team_service.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InsertTeam {

    @NotNull(message = "이름은 필수 조건입니다.")
    private String name;

    private Long upperTeamId;

    private String status;
}
