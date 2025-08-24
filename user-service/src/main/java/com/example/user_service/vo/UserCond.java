package com.example.user_service.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCond {
    Long id;
    String email;
    String name;
    String userId;
    List<String> userIdList;

    @Override
    public String toString() {
        return "UserCond{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", userIdList=" + userIdList +
                '}';
    }
}
