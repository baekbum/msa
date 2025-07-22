package com.example.user_service.jpa;

import com.example.user_service.dto.UserDto;
import com.example.user_service.vo.UserCond;

import java.util.List;

public interface UserRepository {
    User insert(UserDto dto);
    User selectById(String userId);
    List<User> selectByCond(UserCond cond);
    User update(String userId, UserDto dto);
    User delete(String userId);
}
