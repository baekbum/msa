package com.example.catalog_service.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogJpaRepository extends JpaRepository<Catalog, Long> {
    Catalog findByProductId(String productId);
}
