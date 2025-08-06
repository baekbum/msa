package com.example.user_service.service;

import com.example.user_service.client.OrderServiceClient;
import com.example.user_service.dto.UserDto;
import com.example.user_service.jpa.User;
import com.example.user_service.jpa.UserRepository;
import com.example.user_service.vo.ResponseOrder;
import com.example.user_service.vo.UserCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService  {

    private final UserRepository repository;
    private final OrderServiceClient orderServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    public UserDto insert(UserDto dto) {
        return new UserDto(repository.insert(dto));
    }

    @Transactional(readOnly = true)
    public UserDto selectById(String userId) {
        UserDto userDto = new UserDto(repository.selectById(userId));
        userDto.setOrders(getOrders(userId));

        return userDto;
    }

    @Transactional(readOnly = true)
    public List<UserDto> selectByCond(UserCond cond) {
        List<User> userEntities = repository.selectByCond(cond);

        List<UserDto> list = new ArrayList<>();

        userEntities.stream()
                .forEach(entity -> {
                    UserDto userDto = new UserDto(entity);
                    userDto.setOrders(getOrders(userDto.getUserId()));
                    list.add(userDto);
                });

        return list;
    }

    public UserDto delete(String userId) {
        return new UserDto(repository.delete(userId));
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> getOrders(String userId) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        List<ResponseOrder> orders = circuitBreaker.run(
                () -> orderServiceClient.getOrderByUserId(userId).getBody(),
                throwable -> new ArrayList<>());

        return orders;
    }
}
