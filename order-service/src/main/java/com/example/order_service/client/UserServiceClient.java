package com.example.order_service.client;

import com.example.order_service.vo.ResponseUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/exist/{userId}")
    ResponseEntity<?> getUserByUserId(@PathVariable("userId") String userId);
}
