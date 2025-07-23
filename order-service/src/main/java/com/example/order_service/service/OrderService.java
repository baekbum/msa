package com.example.order_service.service;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.jpa.Order;
import com.example.order_service.jpa.OrderRepository;
import com.example.order_service.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;

    public ResponseOrder insert(OrderDto dto) {
        return new ResponseOrder(repository.insert(dto));
    }

    public ResponseOrder selectByOrderId(String orderId) {
        return new ResponseOrder(repository.selectByOrderId(orderId));
    }

    public List<ResponseOrder> selectByUserId(String userId) {
        return repository.selectByUserId(userId).stream()
                .map(ResponseOrder::new)
                .toList();
    }
}
