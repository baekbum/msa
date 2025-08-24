package com.example.order_service.jpa;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.vo.OrderCond;

import java.util.List;

public interface OrderRepository {
    Order insert(OrderDto dto);
    Order selectByOrderId(String orderId);
    List<Order> selectByUserId(String userId);
    List<Order> selectByOrderCond(OrderCond cond);
}
