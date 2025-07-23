package com.example.order_service.dto;

import com.example.order_service.vo.RequestOrder;
import lombok.Data;

@Data
public class OrderDto {
    private String productId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer totalPrice;

    private String orderId;
    private String userId;

    public OrderDto(RequestOrder order) {
        this.productId = order.getProductId();
        this.quantity =  order.getQuantity();
        this.unitPrice = order.getUnitPrice();
    }
}
