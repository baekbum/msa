package com.example.team_service.dto;

import com.example.team_service.jpa.Team;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamDto {
    private Long id;
    private String name;
    private Long upperTeamId;
    private String upperTeamName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TeamDto(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        if (team.getUpperTeam() != null) {
            this.upperTeamId  = team.getUpperTeam().getId();
            this.upperTeamName  = team.getUpperTeam().getName();
        }
        this.status = team.getStatus().name();
        this.createdAt = team.getCreatedAt();
        this.updatedAt = team.getUpdatedAt();
    }
}