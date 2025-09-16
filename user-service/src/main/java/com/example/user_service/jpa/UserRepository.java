package com.example.user_service.jpa;

import com.example.user_service.vo.InsertUser;
import com.example.user_service.vo.UpdateUser;
import com.example.user_service.vo.UserCond;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {
    User insert(InsertUser insertInfo);
    Page<User> selectAll(Pageable pageable);
    User selectById(String userId);
    User selectByEmail(String email);
    Page<User> selectByCond(UserCond cond, Pageable pageable);
    User update(String userId, UpdateUser updateInfo);
    User delete(String userId);
}
