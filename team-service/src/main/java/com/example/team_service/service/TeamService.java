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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository repository;

    /**
     * 팀 추가 메서드
     * @param insertInfo
     * @return
     */
    public TeamDto insert(InsertTeam insertInfo) {
        return new TeamDto(repository.insert(insertInfo));
    }

    /**
     * 팀이 존재하는지 확인 메서드
     * @param teamId
     * @return
     */
    @Transactional(readOnly = true)
    public Boolean existById(Long teamId) {
        Team findTeam = repository.selectById(teamId);
        // 존재하면 true 반환 없으면 exception 터짐
        return TeamStatus.ACTIVE == findTeam.getStatus();
    }

    /**
     * 모든 팀 정보를 가져오는 메서드
     * @param cond
     * @return
     */
    @Transactional(readOnly = true)
    public Page<TeamDto> selectAll(TeamCond cond) {
        PageRequest pageRequest = PageRequest.of(cond.getPage(), cond.getSize(), makeSortInfo(cond.getSort()));
        return repository.selectAll(pageRequest).map(TeamDto::new);
    }

    /**
     * ID 값으로 팀 정보를 가져오는 메서드
     * @param teamId
     * @return
     */
    @Transactional(readOnly = true)
    public TeamDto selectById(Long teamId) {
        TeamDto teamDto = new TeamDto(repository.selectById(teamId));

        return teamDto;
    }

    /**
     * 조검을 통해 팀 정보들을 가져오는 메서드
     * @param cond
     * @return
     */
    @Transactional(readOnly = true)
    public Page<TeamDto> selectByCond(TeamCond cond) {
        PageRequest pageRequest = PageRequest.of(cond.getPage(), cond.getSize(), makeSortInfo(cond.getSort()));
        return repository.selectByCond(cond, pageRequest).map(TeamDto::new);
    }

    /**
     * 팀 수정 메서드
     * @param teamId
     * @param updateInfo
     * @return
     */
    public TeamDto update(Long teamId, UpdateTeam updateInfo) {
        return new TeamDto(repository.update(teamId, updateInfo));
    }

    /**
     * 팀 삭제 메서드
     * @param teamId
     * @return
     */
    public TeamDto delete(Long teamId) {
        return new TeamDto(repository.delete(teamId));
    }

    /**
     * 검색 조건에서 sort 옵션을 처리하기 위한 메서드
     * @param sorts
     * @return
     */
    private Sort makeSortInfo(List<String> sorts) {
        Sort sort = Sort.unsorted();
        if (sorts != null && !sorts.isEmpty()) {
            List<Sort.Order> orders = new ArrayList<>();

            for (String infoStr : sorts) {
                String[] infos = infoStr.split("-");

                if (infos.length == 2) {
                    String field = infos[0];
                    String direction = infos[1];
                    orders.add(new Sort.Order(Sort.Direction.fromString(direction), field));
                }
            }
            sort = Sort.by(orders);
        }

        return sort;
    }
}
