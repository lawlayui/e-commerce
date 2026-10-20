package com.lawlayui.e_commerce.catalog.application.services;

import java.util.List;

import com.lawlayui.e_commerce.catalog.application.mapper.CatalogProductMapper;
import com.lawlayui.e_commerce.catalog.application.port.in.CatalogProductDetailDto;
import com.lawlayui.e_commerce.catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.catalog.application.port.out.CatalogProductRepository;

public class SearchProductsUseCaseImpl {
    private CatalogProductRepository repository;

    public SearchProductsUseCaseImpl(CatalogProductRepository repository) {
        this.repository = repository;
    }

    public List<CatalogProductDetailDto> search(SearchProductQuery query) {
        return CatalogProductMapper.toDtos(repository.search(query, query.page(), query.size()));
    }
}
