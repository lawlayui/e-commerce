package com.lawlayui.e_commerce.catalog.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lawlayui.e_commerce.catalog.application.port.out.CatalogProductRepository;
import com.lawlayui.e_commerce.catalog.application.services.BrowseProductsUseCaseImpl;
import com.lawlayui.e_commerce.catalog.application.services.CreateCatalogProductUseCaseImpl;
import com.lawlayui.e_commerce.catalog.application.services.DeleteCatalogProductByIdUseCaseImpl;
import com.lawlayui.e_commerce.catalog.application.services.GetProductDetailUseCaseImpl;
import com.lawlayui.e_commerce.catalog.application.services.SearchProductsUseCaseImpl;

@Configuration 
public class CatalogProductBeanConfiguration {
    @Bean  
    public BrowseProductsUseCaseImpl browseProductsUseCaseImpl(CatalogProductRepository catalogProductRepository) {
        return new BrowseProductsUseCaseImpl(catalogProductRepository);
    }

    @Bean 
    public CreateCatalogProductUseCaseImpl createCatalogProductUseCaseImpl(CatalogProductRepository catalogProductRepository) {
        return new CreateCatalogProductUseCaseImpl(catalogProductRepository);
    }

    @Bean 
    public DeleteCatalogProductByIdUseCaseImpl deleteCatalogProductByIdUseCaseImpl(CatalogProductRepository catalogProductRepository) {
        return new DeleteCatalogProductByIdUseCaseImpl(catalogProductRepository);
    }

    @Bean 
    public GetProductDetailUseCaseImpl getProductDetailUseCaseImpl(CatalogProductRepository catalogProductRepository) {
        return new GetProductDetailUseCaseImpl(catalogProductRepository);
    }

    @Bean 
    public SearchProductsUseCaseImpl searchProductsUseCaseImpl(CatalogProductRepository catalogProductRepository) {
        return new SearchProductsUseCaseImpl(catalogProductRepository);
    }
}
