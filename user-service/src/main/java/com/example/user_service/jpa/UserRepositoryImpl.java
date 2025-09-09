package com.example.user_service.jpa;

import com.example.user_service.exception.UserDuplicateException;
import com.example.user_service.exception.UserNotExistException;
import com.example.user_service.vo.InsertUser;
import com.example.user_service.vo.UpdateUser;
import com.example.user_service.vo.UserCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    //private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final UserJpaRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User insert(InsertUser insertInfo) {
        if (repository.findByUserId(insertInfo.getId()).isPresent()) {
            throw new UserDuplicateException("해당 사용자 ID는 이미 존재합니다.");
        }

        if (repository.findByEmail(insertInfo.getEmail()).isPresent()) {
            throw new UserDuplicateException("해당 이메일은 이미 존재합니다.");
        }

        insertInfo.setPassword(passwordEncoder.encode(insertInfo.getPassword()));

        User user = new User(insertInfo);
        repository.save(user);

        return user;
    }

    @Override
    public List<User> selectAll() {
        return repository.findAll();
    }

    @Override
    public User selectById(String userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotExistException("해당 유저를 발견하지 못했습니다."));
    }

    @Override
    public User selectByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UserNotExistException("해당 유저를 발견하지 못했습니다."));
    }

    @Override
    public List<User> selectByCond(UserCond cond) {
        QUser user = QUser.user;

        return queryFactory
                .select(user)
                .from(user)
                .where(
                        idEq(cond.getId(), user),
                        emailLike(cond.getEmail(), user),
                        nameLike(cond.getName(), user),
                        userIdLike(cond.getUserId(), user),
                        userIdIn(cond.getUserIdList(), user),
                        teamIdEq(cond.getTeamId(), user),
                        teamIdIn(cond.getTeamIdList(), user)
                ).fetch();
    }

    @Override
    public User update(String userId, UpdateUser updateInfo) {
        User user = selectById(userId);

        user.updateValue(updateInfo);

        return user;
    }

    @Override
    public User delete(String userId) {
        User user = selectById(userId);
        repository.delete(user);
        return user;
    }

    @Override
    public Long findTeam(String userId) {
        return 0L;
    }

    private BooleanExpression idEq(Long id, QUser user) {
        return id != null ? user.id.eq(id) : null;
    }

    private BooleanExpression emailLike(String email, QUser user) {
        return StringUtils.hasText(email) ? user.email.like("%" + email + "%") : null;
    }

    private BooleanExpression nameLike(String name, QUser user) {
        return StringUtils.hasText(name) ? user.name.like("%" + name + "%") : null;
    }

    private BooleanExpression userIdLike(String userIdLike, QUser user) {
        return StringUtils.hasText(userIdLike) ? user.userId.like("%" + userIdLike + "%") : null;
    }

    private BooleanExpression userIdIn(List<String> userIdList, QUser user) {
        return userIdList != null && !userIdList.isEmpty()
                ? user.userId.in(userIdList) : null;
    }

    private BooleanExpression teamIdEq(Long teamId, QUser user) {
        return teamId != null ? user.teamId.eq(teamId) : null;
    }

    private BooleanExpression teamIdIn(List<Long> teamIdList, QUser user) {
        return teamIdList != null && !teamIdList.isEmpty()
                ? user.teamId.in(teamIdList) : null;
    }
}
