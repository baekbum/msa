package com.example.order_service.kafka.queue.producer;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.kafka.dto.Field;
import com.example.order_service.kafka.dto.KafkaOrderDto;
import com.example.order_service.kafka.dto.Payload;
import com.example.order_service.kafka.dto.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderToOrderProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderDto send(String topic, OrderDto dto) {
        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(initSchema(), initPayload(dto));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";

        try {
            jsonString = objectMapper.writeValueAsString(kafkaOrderDto);

            kafkaTemplate.send(topic, jsonString);
            log.info("topic : {}, data : {}", topic, jsonString);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return dto;
    }

    private Payload initPayload(OrderDto dto) {
        return Payload.builder()
                .order_id(dto.getOrderId())
                .user_id(dto.getUserId())
                .product_id(dto.getProductId())
                .quantity(dto.getQuantity())
                .unit_price(dto.getUnitPrice())
                .total_price(dto.getTotalPrice())
                .build();
    }

    private Schema initSchema() {
        return Schema.builder()
                .type("struct")
                .fields(initField())
                .optional(false)
                .name("orders")
                .build();
    }

    private List<Field> initField() {
        return Arrays.asList(
                new Field("string", true, "order_id"),
                new Field("string", true, "user_id"),
                new Field("string", true, "product_id"),
                new Field("int32", true, "quantity"),
                new Field("int32", true, "unit_price"),
                new Field("int32", true, "total_price")
        );
    }
}
