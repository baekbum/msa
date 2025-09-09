package com.example.team_service.service;

import com.example.team_service.dto.TeamDto;
import com.example.team_service.enums.TeamStatus;
import com.example.team_service.jpa.Team;
import com.example.team_service.jpa.TeamRepository;
import com.example.team_service.vo.InsertTeam;
import com.example.team_service.vo.TeamCond;
import com.example.team_service.vo.UpdateTeam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository repository;

    public TeamDto insert(InsertTeam insertInfo) {
        return new TeamDto(repository.insert(insertInfo));
    }

    @Transactional(readOnly = true)
    public Boolean existById(Long teamId) {
        Team findTeam = repository.selectById(teamId);
        // 존재하면 true 반환 없으면 exception 터짐
        return TeamStatus.ACTIVE == findTeam.getStatus();
    }

    @Transactional(readOnly = true)
    public List<TeamDto> selectAll() {
        return repository.selectAll().stream()
                .map(TeamDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamDto selectById(Long teamId) {
        TeamDto teamDto = new TeamDto(repository.selectById(teamId));

        return teamDto;
    }

    @Transactional(readOnly = true)
    public List<TeamDto> selectByCond(TeamCond cond) {
        List<Team> teams = repository.selectByCond(cond);

        return teams.stream()
                .map(TeamDto::new)
                .toList();
    }

    public TeamDto update(Long teamId, UpdateTeam updateInfo) {
        return new TeamDto(repository.update(teamId, updateInfo));
    }

    public TeamDto delete(Long teamId) {
        return new TeamDto(repository.delete(teamId));
    }

}
