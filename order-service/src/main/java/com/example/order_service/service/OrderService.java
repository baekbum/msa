package com.example.order_service.service;

import com.example.order_service.client.UserServiceClient;
import com.example.order_service.dto.OrderDto;
import com.example.order_service.jpa.OrderRepository;
import com.example.order_service.kafka.queue.producer.OrderToCatalogProducer;
import com.example.order_service.kafka.queue.producer.OrderToOrderProducer;
import com.example.order_service.vo.OrderCond;
import com.example.order_service.vo.ResponseOrder;
import com.example.order_service.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final OrderToCatalogProducer orderToCatalogProducer;
    private final OrderToOrderProducer orderToOrderProducer;
    private final UserServiceClient userServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public ResponseOrder insert(OrderDto dto) {
        if (!isExistUser(dto.getUserId())) {
            log.info("{} 유저를 찾을 수 없습니다.", dto.getUserId());
            throw new RuntimeException("해당 유저를 찾을 수 없습니다.");
        }

        dto.setOrderId(UUID.randomUUID().toString());
        dto.setTotalPrice(dto.getQuantity() * dto.getUnitPrice());

        // kafka에 데이터 전달
        orderToCatalogProducer.send("msa-topic-catalog", dto);
        //orderToOrderProducer.send("msa_topic_orders", dto);
        //return new ResponseOrder(dto);

        return new ResponseOrder(repository.insert(dto));
    }

    @Transactional(readOnly = true)
    public ResponseOrder selectByOrderId(String orderId) {
        return new ResponseOrder(repository.selectByOrderId(orderId));
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> selectByUserId(String userId) {
        return repository.selectByUserId(userId).stream()
                .map(ResponseOrder::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> selectByOrderCond(OrderCond cond) {
        return repository.selectByOrderCond(cond).stream()
                .map(ResponseOrder::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public Boolean isExistUser(String userId) {
        log.info("circuitBreaker start");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        Boolean isExist = circuitBreaker.run(
                () -> {
                    HttpStatusCode statusCode = userServiceClient.getUserByUserId(userId).getStatusCode();
                    return HttpStatus.OK == statusCode;
                },
                throwable -> {
                    log.error("CircuitBreaker fallback triggered. Cause: {}", throwable.getMessage());
                    return false;
                }
        );

        log.info("circuitBreaker end");
        log.info("isExist : {}", isExist);

        return isExist;
    }
}
