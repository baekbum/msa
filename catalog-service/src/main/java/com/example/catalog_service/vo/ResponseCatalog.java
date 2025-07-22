package com.example.catalog_service.vo;

import com.example.catalog_service.dto.CatalogDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCatalog {
    private String productId;
    private String productName;
    private Integer unitPrice;
    private Integer quantity;
    private Date createdAt;

    public ResponseCatalog(CatalogDto dto) {
        this.productId = dto.getProductId();
        this.productName = dto.getProductName();
        this.unitPrice = dto.getUnitPrice();
        this.quantity = dto.getQuantity();
        this.createdAt = dto.getCreatedAt();
    }
}
