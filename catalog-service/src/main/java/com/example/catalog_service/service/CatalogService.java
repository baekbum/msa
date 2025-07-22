package com.example.catalog_service.service;

import com.example.catalog_service.dto.CatalogDto;
import com.example.catalog_service.jpa.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogService {
    private final CatalogRepository repository;

    public List<CatalogDto> selectAll() {
        return repository.selectAll().stream()
                .map(CatalogDto::new)
                .toList();
    }
}
