package com.lawlayui.e_commerce.catalog.application.port.out;

import java.util.List;
import java.util.Optional;

import com.lawlayui.e_commerce.catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.catalog.domain.CatalogProduct;

public interface CatalogProductRepository {
    void save(CatalogProduct catalogProduct);
    void deleteById(String id);
    Optional<CatalogProduct> getById(String id);
    List<CatalogProduct> getAll(int page, int size);
    List<CatalogProduct> search(SearchProductQuery query, int page, int size);
}
