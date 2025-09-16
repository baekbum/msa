package com.example.team_service.jpa;

import com.example.team_service.vo.InsertTeam;
import com.example.team_service.vo.TeamCond;
import com.example.team_service.vo.UpdateTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamRepository {
    Team insert(InsertTeam insertInfo);
    Page<Team> selectAll(Pageable pageable);
    Team selectById(Long teamId);
    Page<Team> selectByCond(TeamCond cond, Pageable pageable);
    Team update(Long teamId, UpdateTeam updateInfo);
    Team delete(Long teamId);
}
