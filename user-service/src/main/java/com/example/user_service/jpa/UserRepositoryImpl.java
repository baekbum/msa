package com.example.user_service.jpa;

import com.example.user_service.dto.UserDto;
import com.example.user_service.exception.UserDuplicateException;
import com.example.user_service.exception.UserNotExistException;
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
    public User insert(UserDto dto) {
        if (repository.findByUserId(dto.getUserId()).isPresent()) {
            throw new UserDuplicateException("해당 사용자 ID는 이미 존재합니다.");
        }

        if (repository.findByEmail(dto.getUserId()).isPresent()) {
            throw new UserDuplicateException("해당 이메일은 이미 존재합니다.");
        }

        dto.setUserId(dto.getUserId());
        User user = new User(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        repository.save(user);

        return user;
    }

    @Override
    public List<User> selectAll() {
        return repository.findAll();
    }

    @Override
    public User selectById(String userId) {
        User foundUser = repository.findByUserId(userId)
                .orElseThrow(() -> new UserNotExistException("해당 유저를 발견하지 못했습니다."));

        return foundUser;
    }

    @Override
    public User selectByEmail(String email) {
        User foundUser = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotExistException("해당 유저를 발견하지 못했습니다."));

        return foundUser;
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
                        userIdIn(cond.getUserIdList(), user)
                ).fetch();
    }

    @Override
    public User update(String userId, UserDto dto) {
        // 현재는 수정기능 추가 예정 없음!
        User user = selectById(userId);
        // 엔티티 업데이트 작업
        return user;
    }

    @Override
    public User delete(String userId) {
        User user = selectById(userId);
        repository.delete(user);
        return user;
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

    private BooleanExpression userIdIn(List<String> userIdList, QUser order) {
        return !userIdList.isEmpty() ?  order.userId.in(userIdList) : null;
    }
}
