package com.example.catalog_service.jpa;

import java.util.List;

public interface CatalogRepository {
    List<Catalog> selectAll();
    Catalog selectByProductIdAndUpdateQuantity(String productId);
}
