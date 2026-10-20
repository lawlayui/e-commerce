package com.lawlayui.e_commerce.catalog.application.services;

import com.lawlayui.e_commerce.catalog.application.exception.CatalogProductNotFoundException;
import com.lawlayui.e_commerce.catalog.application.mapper.CatalogProductMapper;
import com.lawlayui.e_commerce.catalog.application.port.in.CatalogProductDetailDto;
import com.lawlayui.e_commerce.catalog.application.port.out.CatalogProductRepository;
import com.lawlayui.e_commerce.catalog.domain.CatalogProduct;

public class GetProductDetailUseCaseImpl {
    private CatalogProductRepository repository;
    
    public GetProductDetailUseCaseImpl(CatalogProductRepository repository) {
        this.repository = repository;
    }

    public CatalogProductDetailDto getById(String id) {
        CatalogProduct catalogProduct = repository.getById(id) 
            .orElseThrow(() -> new CatalogProductNotFoundException("Product with id " + id + " not found"));

        return CatalogProductMapper.toDto(catalogProduct);
    }
}