package com.example.order_service.jpa;

import com.example.order_service.dto.OrderDto;

import java.util.List;

public interface OrderRepository {
    Order insert(OrderDto dto);
    Order selectByOrderId(String orderId);
    List<Order> selectByUserId(String userId);
}
