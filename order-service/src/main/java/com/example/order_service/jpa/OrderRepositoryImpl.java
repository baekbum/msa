package com.example.order_service.jpa;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.vo.OrderCond;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JPAQueryFactory queryFactory;
    private final OrderJpaRepository repository;

    @Override
    public Order insert(OrderDto dto) {
        dto.setOrderId(UUID.randomUUID().toString());
        dto.setTotalPrice(dto.getQuantity() * dto.getUnitPrice());

        Order newOrder = new Order(dto);

        repository.save(newOrder);

        return newOrder;
    }

    @Override
    public Order selectByOrderId(String orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("주문 정보를 확인할 수 없습니다."));
    }

    @Override
    public List<Order> selectByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<Order> selectByOrderCond(OrderCond cond) {
        QOrder order = QOrder.order;

        return queryFactory
                .select(order)
                .from(order)
                .where(
                        userIdIn(cond.getUserIdList(), order),
                        orderIdIn(cond.getOrderIdList(), order)
                ).fetch();
    }

    private BooleanExpression userIdIn(List<String> userIdList, QOrder order) {
        return userIdList != null && !userIdList.isEmpty() ?  order.userId.in(userIdList) : null;
    }

    private BooleanExpression orderIdIn(List<String> orderIdList, QOrder order) {
        return orderIdList != null && !orderIdList.isEmpty() ?  order.orderId.in(orderIdList) : null;
    }
}
