package com.example.order_service.service;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.jpa.OrderRepository;
import com.example.order_service.kafka.queue.producer.OrderToCatalogProducer;
import com.example.order_service.kafka.queue.producer.OrderToOrderProducer;
import com.example.order_service.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public ResponseOrder insert(OrderDto dto) {
        dto.setOrderId(UUID.randomUUID().toString());
        dto.setTotalPrice(dto.getQuantity() * dto.getUnitPrice());

        // kafka에 데이터 전달
        orderToCatalogProducer.send("msa-topic-catalog", dto);
        orderToOrderProducer.send("msa_topic_orders", dto);

        return new ResponseOrder(dto);

        // 기존 JPA를 사용한 로직은 더 이상 사용하지 않음
        //return new ResponseOrder(repository.insert(dto));
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
}
