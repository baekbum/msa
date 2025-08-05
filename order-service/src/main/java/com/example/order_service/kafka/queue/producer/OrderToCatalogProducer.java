package com.example.order_service.kafka.queue.producer;

import com.example.order_service.dto.OrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderToCatalogProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderDto send(String topic, OrderDto dto) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = "";

        try {
            jsonString = objectMapper.writeValueAsString(dto);

            kafkaTemplate.send(topic, jsonString);
            log.info("topic : {}, data : {}", topic, jsonString);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return dto;
    }
}
