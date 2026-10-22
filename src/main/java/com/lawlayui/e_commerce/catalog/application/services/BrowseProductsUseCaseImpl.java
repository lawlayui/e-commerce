package com.lawlayui.e_commerce.catalog.application.services;

import java.util.List;

import com.lawlayui.e_commerce.catalog.application.mapper.CatalogProductMapper;
import com.lawlayui.e_commerce.catalog.application.port.in.CatalogProductDto;
import com.lawlayui.e_commerce.catalog.application.port.out.CatalogProductRepository;

public class BrowseProductsUseCaseImpl {
    private CatalogProductRepository repository;
    
    public BrowseProductsUseCaseImpl(CatalogProductRepository repository) {
        this.repository = repository;
    }

    public List<CatalogProductDto> browse(int page, int size) {
        return repository.getAll(page, size).stream()
            .map(CatalogProductMapper::toCatalogProductDto)
            .toList();
    }
}
