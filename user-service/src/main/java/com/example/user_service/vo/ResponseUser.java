package com.example.user_service.vo;

import com.example.user_service.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseUser {
    private String email;
    private String name;
    private String userId;
    private List<ResponseOrder> orders;

    public ResponseUser(UserDto dto) {
        this.email = dto.getEmail();
        this.name = dto.getName();
        this.userId = dto.getUserId();
        this.orders = dto.getOrders();
    }
}