package com.example.user_service.jpa;

import com.example.user_service.dto.UserDto;
import com.example.user_service.vo.InsertUser;
import com.example.user_service.vo.ResponseOrder;
import com.example.user_service.vo.UpdateUser;
import com.example.user_service.vo.UserCond;

import java.util.List;

public interface UserRepository {
    User insert(InsertUser insertInfo);
    List<User> selectAll();
    User selectById(String userId);
    User selectByEmail(String email);
    List<User> selectByCond(UserCond cond);
    User update(String userId, UpdateUser updateInfo);
    User delete(String userId);

    Long findTeam(String userId);
}
