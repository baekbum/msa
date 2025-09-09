package com.example.team_service.jpa;

import com.example.team_service.dto.TeamDto;
import com.example.team_service.vo.InsertTeam;
import com.example.team_service.vo.TeamCond;
import com.example.team_service.vo.UpdateTeam;

import java.util.List;

public interface TeamRepository {
    Team insert(InsertTeam insertInfo);
    List<Team> selectAll();
    Team selectById(Long teamId);
    List<Team> selectByCond(TeamCond cond);
    Team update(Long teamId, UpdateTeam updateInfo);
    Team delete(Long teamId);
}
