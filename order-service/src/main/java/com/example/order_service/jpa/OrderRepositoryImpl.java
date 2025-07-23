package com.example.order_service.jpa;

import com.example.order_service.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

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
}
