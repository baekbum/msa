package com.example.catalog_service.dto;

import com.example.catalog_service.jpa.Catalog;
import lombok.Data;

import java.util.Date;

@Data
public class CatalogDto {
    private String productId;
    private String productName;
    private Integer quantity;
    private Integer unitPrice;
    private Date createdAt;

    private Integer totalPrice;
    private String orderId;
    private String userId;

    public CatalogDto(Catalog catalog) {
        this.productId = catalog.getProductId();
        this.productName = catalog.getProductName();
        this.quantity = catalog.getQuantity();
        this.unitPrice = catalog.getUnitPrice();
        this.createdAt = catalog.getCreatedAt();
    }
}
