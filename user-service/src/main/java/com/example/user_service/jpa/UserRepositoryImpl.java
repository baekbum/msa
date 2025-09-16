package com.example.user_service.jpa;

import com.example.user_service.exception.UserDuplicateException;
import com.example.user_service.exception.UserNotExistException;
import com.example.user_service.vo.InsertUser;
import com.example.user_service.vo.UpdateUser;
import com.example.user_service.vo.UserCond;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
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
    public Page<User> selectAll(Pageable pageable) {
        return repository.findAll(pageable);
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
    public Page<User> selectByCond(UserCond cond, Pageable pageable) {
        QUser user = QUser.user;

        // 1. Pageable 객체에서 Sort 정보를 추출하여 OrderSpecifier 리스트를 만듭니다.
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
        pageable.getSort().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder<User> entityPath = new PathBuilder<>(User.class, "user");
            orderSpecifiers.add(new OrderSpecifier(direction, entityPath.get(property)));

            // Q-class를 사용하여 각 필드에 대한 정렬 조건을 명시적으로 추가합니다.
//            switch (property) {
//                case "id":
//                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.id));
//                    break;
//                case "userId":
//                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.userId));
//                    break;
//                case "name":
//                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.name));
//                    break;
//                case "email":
//                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.email));
//                    break;
//                case "teamId":
//                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.teamId));
//                    break;
//                case "status":
//                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.status));
//                    break;
//                case "createAt":
//                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.createAt));
//                    break;
//                case "updatedAt":
//                    orderSpecifiers.add(new OrderSpecifier<>(direction, user.updatedAt));
//                    break;
//                default:
//                    // 정렬 기준이 되는 필드를 찾지 못한 경우
//                    // 예외를 던지거나 기본 정렬 조건을 추가할 수 있습니다.
//                    throw new IllegalArgumentException("Invalid sort property: " + property);
//            }
        });

        List<User> content = queryFactory
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
                )
                .offset(pageable.getOffset()) // 오프셋 적용
                .limit(pageable.getPageSize()) // 페이지 크기 적용
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])) // 정렬 정보 적용
                .fetch();

        Long total = queryFactory
                .select(user.count())
                .from(user)
                .where(
                        idEq(cond.getId(), user),
                        emailLike(cond.getEmail(), user),
                        nameLike(cond.getName(), user),
                        userIdLike(cond.getUserId(), user),
                        userIdIn(cond.getUserIdList(), user),
                        teamIdEq(cond.getTeamId(), user),
                        teamIdIn(cond.getTeamIdList(), user)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
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
