package com.example.user_service.jpa;

import com.example.user_service.dto.UserDto;
import com.example.user_service.vo.UserCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    //private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final UserJpaRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public User insert(UserDto dto) {
        dto.setUserId(UUID.randomUUID().toString());
        User user = new User(dto);
        user.setEncryptedPwd(passwordEncoder.encode(dto.getPwd()));

        repository.save(user);

        return user;
    }

    @Override
    public User selectById(String userId) {
        User foundUser = repository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return foundUser;
    }

    @Override
    public User selectByEmail(String email) {
        User foundUser = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                        userIdLike(cond.getUserId(), user)
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
}
