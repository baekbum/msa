package com.example.team_service.vo;

import com.example.team_service.jpa.Team;
import lombok.Data;

@Data
public class UpdateTeam {
    private String name;
    private Long upperTeamId;
    private String status;

    private Team upperTeam;
}
