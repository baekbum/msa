package com.example.user_service.service;

import com.example.user_service.dto.UserDto;
import com.example.user_service.jpa.User;
import com.example.user_service.jpa.UserRepository;
import com.example.user_service.vo.UserCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public UserDto insert(UserDto dto) {
        return new UserDto(repository.insert(dto));
    }

    @Transactional(readOnly = true)
    public UserDto selectById(String userId) {
        UserDto userDto = new UserDto(repository.selectById(userId));
        userDto.setOrders(new ArrayList<>());

        return userDto;
    }

    @Transactional(readOnly = true)
    public List<UserDto> selectByCond(UserCond cond) {
        List<User> userEntities = repository.selectByCond(cond);

        List<UserDto> list = new ArrayList<>();

        userEntities.stream()
                .forEach(entity -> {
                    UserDto userDto = new UserDto(entity);
                    userDto.setOrders(new ArrayList<>());
                    list.add(userDto);
                });

        return list;
    }

    public UserDto delete(String userId) {
        return new UserDto(repository.delete(userId));
    }
}
