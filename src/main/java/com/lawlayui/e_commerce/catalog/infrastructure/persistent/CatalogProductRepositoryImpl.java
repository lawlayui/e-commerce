package com.lawlayui.e_commerce.catalog.infrastructure.persistent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.lawlayui.e_commerce.catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.catalog.application.port.out.CatalogProductRepository;
import com.lawlayui.e_commerce.catalog.domain.CatalogProduct;

public class CatalogProductRepositoryImpl implements CatalogProductRepository{
    private CatalogProductJpaRepository repository; 

    public CatalogProductRepositoryImpl(CatalogProductJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }

    @Override
    public List<CatalogProduct> getAll(int page, int size) {
        return repository.findAll(PageRequest.of(page, size))
            .map(CatalogProductPersistentMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<CatalogProduct> getById(String id) {
        return repository.findById(id)
            .map(CatalogProductPersistentMapper::toDomain);
    }

    @Override
    public void save(CatalogProduct catalogProduct) {

    }

    @Override
    public List<CatalogProduct> search(SearchProductQuery query, int page, int size) {
        return repository.findByFilters(query.startingPrice(), query.finalPrice(), query.rating(), query.name(), query.category(), Sort.by("price"))
            .stream()
            .map(CatalogProductPersistentMapper::toDomain)
            .toList();
    }
}
