package com.example.user_service.service;

import com.example.user_service.client.OrderServiceClient;
import com.example.user_service.dto.UserDto;
import com.example.user_service.jpa.User;
import com.example.user_service.jpa.UserRepository;
import com.example.user_service.vo.OrderCond;
import com.example.user_service.vo.ResponseOrder;
import com.example.user_service.vo.UserCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
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
    public Boolean existById(String userId) {
        UserDto userDto = new UserDto(repository.selectById(userId));
        log.info("userDto : {}", userDto);

        return StringUtils.hasText(userDto.getUserId());
    }

    @Transactional(readOnly = true)
    public UserDto selectById(String userId) {
        UserDto userDto = new UserDto(repository.selectById(userId));
        userDto.setOrders(getOrders(userId));

        log.info("userDto : {}", userDto);

        return userDto;
    }

    @Transactional(readOnly = true)
    public List<UserDto> selectByCond(UserCond cond) {
        List<User> userEntities = repository.selectByCond(cond);

        // 조건에 맞는 UserId를 파라미터로 넘겨서 주문 내역을 가져옴
        HashMap<String, List<ResponseOrder>> map = orderListToMap(getOrdersWithUserIdList(userEntities));
        List<UserDto> list = new ArrayList<>();

        userEntities.stream()
                .forEach(entity -> {
                    UserDto userDto = new UserDto(entity);
                    userDto.setOrders(map.get(entity.getUserId()));
                    list.add(userDto);
                });

        return list;
    }

    public UserDto delete(String userId) {
        return new UserDto(repository.delete(userId));
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> getOrders(String userId) {
        log.info("circuitBreaker start");

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        List<ResponseOrder> orders = circuitBreaker.run(
                () -> orderServiceClient.getOrderByUserId(userId).getBody(),
                throwable -> {
                    log.error("CircuitBreaker fallback triggered. Cause: {}", throwable.getMessage());
                    return new ArrayList<>();
                }
        );

        log.info("circuitBreaker end");

        return orders;
    }

    @Transactional(readOnly = true)
    public List<ResponseOrder> getOrdersWithUserIdList(List<User> userEntities) {
        log.info("circuitBreaker start");

        List<String> userIdList = userEntities.stream()
                .map(User::getUserId)
                .toList();

        OrderCond cond = new OrderCond();
        cond.setUserIdList(userIdList);

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitBreaker");
        List<ResponseOrder> orders = circuitBreaker.run(
                () -> orderServiceClient.getOrdersWithCond(cond).getBody(),
                throwable -> {
                    log.error("CircuitBreaker fallback triggered. Cause: {}", throwable.getMessage());
                    return new ArrayList<>();
                }
        );

        log.info("circuitBreaker end");

        return orders;
    }

    private HashMap<String, List<ResponseOrder>> orderListToMap(List<ResponseOrder> responseOrders) {
        HashMap<String, List<ResponseOrder>> map = new HashMap<>();

        for (ResponseOrder order : responseOrders) {
            String userId = order.getUserId();
            if (!map.containsKey(userId)) {
                map.put(userId, new ArrayList<>());
            }
            map.get(userId).add(order);
        }

        return map;
    }
}
