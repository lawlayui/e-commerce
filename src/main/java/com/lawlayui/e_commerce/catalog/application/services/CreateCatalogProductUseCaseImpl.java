package com.lawlayui.e_commerce.catalog.application.services;

import com.lawlayui.e_commerce.catalog.application.mapper.CatalogProductMapper;
import com.lawlayui.e_commerce.catalog.application.port.in.CreateCatalogProductCommand;
import com.lawlayui.e_commerce.catalog.application.port.out.CatalogProductRepository;

public class CreateCatalogProductUseCaseImpl {
    private CatalogProductRepository repository; 

    public CreateCatalogProductUseCaseImpl(CatalogProductRepository repository) {
        this.repository = repository;
    }

    public void create(CreateCatalogProductCommand command) {
        repository.save(CatalogProductMapper.commandToDomain(command));
    }
}
