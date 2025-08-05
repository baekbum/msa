package com.example.order_service.vo;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.jpa.Order;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseOrder {
    private String productId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;
    private Date createdAt;

    private String orderId;

    public ResponseOrder(Order order) {
        this.orderId = order.getOrderId();
        this.productId = order.getProductId();
        this.quantity = order.getQuantity();
        this.unitPrice = order.getUnitPrice();
        this.totalPrice = order.getTotalPrice();
        this.createdAt = order.getCreatedAt();
    }

    public ResponseOrder(OrderDto dto) {
        this.orderId = dto.getOrderId();
        this.productId = dto.getProductId();
        this.quantity = dto.getQuantity();
        this.unitPrice = dto.getUnitPrice();
        this.totalPrice = dto.getTotalPrice();
    }


}
