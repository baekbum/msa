package com.example.order_service.controller;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.service.OrderService;
import com.example.order_service.vo.RequestOrder;
import com.example.order_service.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("add/{userId}")
    public ResponseEntity<?> createOrder(@PathVariable("userId") String userId, @RequestBody RequestOrder orderDetails) {
        log.info("Before add orders data");

        OrderDto orderDto = new OrderDto(orderDetails);
        orderDto.setUserId(userId);

        ResponseOrder responseOrder = service.insert(orderDto);

        log.info("After added orders data");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("search/user/{userId}")
    public ResponseEntity<?> getOrderByUserId(@PathVariable("userId") String userId) throws Exception {
        log.info("Before retrieve orders data");

        List<ResponseOrder> responseOrders = service.selectByUserId(userId);

        log.info("Add retrieved orders data");

        return ResponseEntity.status(HttpStatus.OK).body(responseOrders);
    }

    @GetMapping("search/order/{orderId}")
    public ResponseEntity<?> getOrderByOrderId(@PathVariable("orderId") String orderId) throws Exception {
        log.info("Before retrieve orders data");

        ResponseOrder responseOrder = service.selectByOrderId(orderId);

        log.info("Add retrieved orders data");

        return ResponseEntity.status(HttpStatus.OK).body(responseOrder);
    }
}
