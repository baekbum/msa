package com.example.user_service.client;

import com.example.user_service.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/search/user/{userId}")
    ResponseEntity<List<ResponseOrder>> getOrderByUserId(@PathVariable("userId") String userId);
}
