package com.example.user_service.client;

import com.example.user_service.vo.OrderCond;
import com.example.user_service.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    @GetMapping("/search/user/{userId}")
    ResponseEntity<List<ResponseOrder>> getOrderByUserId(@PathVariable("userId") String userId);

    @PostMapping ("/search/orders")
    ResponseEntity<List<ResponseOrder>> getOrdersWithCond(@RequestBody OrderCond cond);
}
