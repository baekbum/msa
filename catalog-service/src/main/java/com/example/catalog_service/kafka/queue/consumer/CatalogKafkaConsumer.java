package com.example.catalog_service.kafka.queue.consumer;

import com.example.catalog_service.jpa.Catalog;
import com.example.catalog_service.service.CatalogService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CatalogKafkaConsumer {

    private final CatalogService catalogService;

    @KafkaListener(topics = "msa-topic-catalog")
    public void updateQuantity(String kafkaMessage) {
        log.info("kafkaMessage : {}", kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            map = objectMapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});

            log.info("Map data : {}", map);

            Catalog catalog = catalogService.selectByProductIdAndUpdateQuantity((String) map.get("productId"));

            int stock = catalog.getQuantity();
            int orderQuantity = (int) map.get("quantity");

            if (stock - orderQuantity < 0)
                throw new RuntimeException("재고 부족");

            log.info("result quantity : {}", stock - orderQuantity);
            catalog.setQuantity(stock - orderQuantity);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
