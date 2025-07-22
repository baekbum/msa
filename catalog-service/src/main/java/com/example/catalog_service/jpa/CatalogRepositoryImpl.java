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
}
