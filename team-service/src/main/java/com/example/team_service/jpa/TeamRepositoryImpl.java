package com.example.team_service.jpa;

import com.example.team_service.enums.TeamStatus;
import com.example.team_service.exception.TeamDuplicateException;
import com.example.team_service.exception.TeamNotExistException;
import com.example.team_service.vo.InsertTeam;
import com.example.team_service.vo.TeamCond;
import com.example.team_service.vo.UpdateTeam;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TeamRepositoryImpl implements TeamRepository {

    private final JPAQueryFactory queryFactory;
    private final TeamJpaRepository repository;

    @Override
    public Team insert(InsertTeam insertInfo) {
        if (repository.findByName(insertInfo.getName()).isPresent()) {
            throw new TeamDuplicateException("해당 팀 이름은 이미 존재합니다.");
        }

        Team team = insertInfo.getUpperTeamId() != null
                ? new Team(insertInfo, selectById(insertInfo.getUpperTeamId()))
                : new Team(insertInfo);

        repository.save(team);

        return team;
    }

    @Override
    public Page<Team> selectAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Team selectById(Long teamId) {
        return repository.findById(teamId)
                .orElseThrow(() -> new TeamNotExistException("해당 팀은 존재하지 않습니다."));
    }

    @Override
    public Page<Team> selectByCond(TeamCond cond, Pageable pageable) {
        QTeam team = QTeam.team;

        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder<Team> entityPath = new PathBuilder<>(Team.class, "team");
            orderSpecifiers.add(new OrderSpecifier(direction, entityPath.get(property)));
        });

        List<Team> content = queryFactory
                .select(team)
                .from(team)
                .where(
                        idEq(cond.getId(), team),
                        nameLike(cond.getName(), team),
                        upperTeamIdEq(cond.getUpperTeamId(), team),
                        upperTeamNameLike(cond.getUpperTeamName(), team),
                        statusEq(cond.getStatus(), team),
                        teamIdIn(cond.getTeamIdList(), team),
                        teamNameIn(cond.getTeamNameList(), team),
                        upperTeamIdIn(cond.getUpperTeamIdList(), team),
                        upperTeamNameIn(cond.getUpperTeamNameList(), team)
                )
                .offset(pageable.getOffset()) // 오프셋 적용
                .limit(pageable.getPageSize()) // 페이지 크기 적용
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])) // 정렬 정보 적용
                .fetch();

        Long total = queryFactory
                .select(team.count())
                .from(team)
                .where(
                        idEq(cond.getId(), team),
                        nameLike(cond.getName(), team),
                        upperTeamIdEq(cond.getUpperTeamId(), team),
                        upperTeamNameLike(cond.getUpperTeamName(), team),
                        statusEq(cond.getStatus(), team),
                        teamIdIn(cond.getTeamIdList(), team),
                        teamNameIn(cond.getTeamNameList(), team),
                        upperTeamIdIn(cond.getUpperTeamIdList(), team),
                        upperTeamNameIn(cond.getUpperTeamNameList(), team)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Team update(Long teamId, UpdateTeam updateInfo) {
        Team findTeam = selectById(teamId);

        if (updateInfo.getUpperTeamId() != null) {
            updateInfo.setUpperTeam(selectById(updateInfo.getUpperTeamId()));
        }

        findTeam.updateTeam(updateInfo);

        return findTeam;
    }

    @Override
    public Team delete(Long teamId) {
        Team findTeam = selectById(teamId);
        repository.delete(findTeam);

        return findTeam;
    }

    private BooleanExpression idEq(Long id, QTeam team) {
        return id != null ? team.id.eq(id) : null;
    }

    private BooleanExpression nameLike(String name, QTeam team) {
        return StringUtils.hasText(name) ? team.name.like("%" + name + "%") : null;
    }

    private BooleanExpression upperTeamIdEq(Long upperTeamId, QTeam team) {
        return upperTeamId != null ? team.upperTeam.id.eq(upperTeamId) : null;
    }

    private BooleanExpression upperTeamNameLike(String upperTeamName, QTeam team) {
        return StringUtils.hasText(upperTeamName) ? team.upperTeam.name.like("%" + upperTeamName + "%") : null;
    }

    private BooleanExpression statusEq(String status, QTeam team) {
        return StringUtils.hasText(status) ? team.status.eq(TeamStatus.valueOf(status)) : null;
    }

    private BooleanExpression teamIdIn(List<Long> teamIdList, QTeam team) {
        return teamIdList != null && !teamIdList.isEmpty()
                ? team.id.in(teamIdList)
                : null;
    }

    private BooleanExpression teamNameIn(List<String> teamNameList, QTeam team) {
        return teamNameList != null && !teamNameList.isEmpty()
                ? team.name.in(teamNameList)
                : null;
    }

    private BooleanExpression upperTeamIdIn(List<Long> upperTeamIdList, QTeam team) {
        return upperTeamIdList != null && !upperTeamIdList.isEmpty()
                ? team.upperTeam.id.in(upperTeamIdList)
                : null;
    }

    private BooleanExpression upperTeamNameIn(List<String> upperTeamNameList, QTeam team) {
        return upperTeamNameList != null && !upperTeamNameList.isEmpty()
                ? team.upperTeam.name.in(upperTeamNameList)
                : null;
    }
}
