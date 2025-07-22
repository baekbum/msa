package com.example.catalog_service.controller;

import com.example.catalog_service.dto.CatalogDto;
import com.example.catalog_service.service.CatalogService;
import com.example.catalog_service.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class CatalogController {
    private final CatalogService service;

    @GetMapping("/search")
    public ResponseEntity<?> getCatalogs() {
        List<CatalogDto> catalogList = service.selectAll();

        List<ResponseCatalog> result = catalogList.stream()
                .map(ResponseCatalog::new)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
