package com.example.user_service.service;

import com.example.user_service.dto.UserDto;
import com.example.user_service.jpa.UserEntity;
import com.example.user_service.jpa.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    Environment env;

    UserRepository userRepository;

    public UserServiceImpl(Environment env, UserRepository userRepository) {
        this.env = env;
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        UserEntity userEntity = new UserEntity(userDto);
        userEntity.setEncryptedPwd("encrypted_password");

        userRepository.save(userEntity);

        UserDto returnUserDto = new UserDto(userEntity);

        return returnUserDto;
    }
}