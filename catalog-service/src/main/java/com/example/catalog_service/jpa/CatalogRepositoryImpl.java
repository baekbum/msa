package com.example.catalog_service.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CatalogRepositoryImpl implements CatalogRepository {
    private final CatalogJpaRepository repository;

    @Override
    public List<Catalog> selectAll() {
        return repository.findAll();
    }
    @Override
    public Catalog selectByProductIdAndUpdateQuantity(String productId) {
        return repository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품 ID를 찾지 못함"));
    }
}
