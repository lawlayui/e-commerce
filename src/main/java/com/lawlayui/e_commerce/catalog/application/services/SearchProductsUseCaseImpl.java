package com.lawlayui.e_commerce.catalog.application.services;

import java.util.List;

import com.lawlayui.e_commerce.catalog.application.mapper.CatalogProductMapper;
import com.lawlayui.e_commerce.catalog.application.port.in.CatalogProductDto;
import com.lawlayui.e_commerce.catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.catalog.application.port.out.CatalogProductRepository;

public class SearchProductsUseCaseImpl {
    private CatalogProductRepository repository;

    public SearchProductsUseCaseImpl(CatalogProductRepository repository) {
        this.repository = repository;
    }

    public List<CatalogProductDto> search(SearchProductQuery query) {
        return repository.search(query, query.page(), query.size()).stream()
            .map(CatalogProductMapper::toCatalogProductDto)
            .toList();
    }
}
