package com.lawlayui.e_commerce.catalog.infrastructure.web;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lawlayui.e_commerce.catalog.application.port.in.CatalogProductDetailDto;
import com.lawlayui.e_commerce.catalog.application.port.in.CatalogProductDto;
import com.lawlayui.e_commerce.catalog.application.port.in.SearchProductQuery;
import com.lawlayui.e_commerce.catalog.application.services.BrowseProductsUseCaseImpl;
import com.lawlayui.e_commerce.catalog.application.services.GetProductDetailUseCaseImpl;
import com.lawlayui.e_commerce.catalog.application.services.SearchProductsUseCaseImpl;

@RestController 
@RequestMapping("/api/v1/catalog")
public class CatalogProductController {
    private BrowseProductsUseCaseImpl browseProductsUseCaseImpl;
    private GetProductDetailUseCaseImpl getProductDetailUseCaseImpl;
    private SearchProductsUseCaseImpl searchProductsUseCaseImpl;

    public CatalogProductController(BrowseProductsUseCaseImpl browseProductsUseCaseImpl, GetProductDetailUseCaseImpl getProductDetailUseCaseImpl, SearchProductsUseCaseImpl searchProductsUseCaseImpl) {
        this.browseProductsUseCaseImpl = browseProductsUseCaseImpl;
        this.getProductDetailUseCaseImpl = getProductDetailUseCaseImpl;
        this.searchProductsUseCaseImpl = searchProductsUseCaseImpl;
    }

    @GetMapping("/{id}")
    public CatalogProductDetailDto getCatalogProductDetail(@PathVariable String id) {
        return getProductDetailUseCaseImpl.getById(id);
    }

    @GetMapping
    public List<CatalogProductDto> browse(@RequestParam int size, @RequestParam int page) {
        return browseProductsUseCaseImpl.browse(page, size);
    }

    @GetMapping
    public List<CatalogProductDto> search(@RequestParam int size, @RequestParam int page, @RequestParam String name, @RequestParam String category,@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice, @RequestParam double rating) {
        return searchProductsUseCaseImpl.search(new SearchProductQuery(page, size, name, category, minPrice, maxPrice, rating));
    }
}
