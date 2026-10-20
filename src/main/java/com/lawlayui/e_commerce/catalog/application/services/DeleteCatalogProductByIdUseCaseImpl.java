package com.lawlayui.e_commerce.catalog.application.services;

import com.lawlayui.e_commerce.catalog.application.port.out.CatalogProductRepository;

public class DeleteCatalogProductByIdUseCaseImpl {
    private CatalogProductRepository repository;

    public DeleteCatalogProductByIdUseCaseImpl(CatalogProductRepository repository) {
        this.repository = repository;
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
