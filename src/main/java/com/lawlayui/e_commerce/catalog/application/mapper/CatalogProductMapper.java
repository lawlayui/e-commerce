package com.lawlayui.e_commerce.catalog.application.mapper;

import java.util.List;

import com.lawlayui.e_commerce.catalog.application.port.in.CatalogProductDetailDto;
import com.lawlayui.e_commerce.catalog.application.port.in.CreateCatalogProductCommand;
import com.lawlayui.e_commerce.catalog.domain.CatalogProduct;
import com.lawlayui.e_commerce.catalog.domain.value_object.Category;

public class CatalogProductMapper {
    public static CatalogProductDetailDto toDto(CatalogProduct catalogProduct) {
        List<String>  categories = catalogProduct.getCategories().stream()
            .map((cateogry) -> cateogry.getCategory()).toList();
        return new CatalogProductDetailDto(catalogProduct.getCatalogProductId(), catalogProduct.getSku().getValue(), catalogProduct.getSellerId(), catalogProduct.getName(),
            catalogProduct.getDescription(), catalogProduct.getPrice().getValue(), categories, catalogProduct.getImageUrl(), catalogProduct.getAvailability().name(),
            catalogProduct.getAvargeRating().getValue(), catalogProduct.getReviewCount(), catalogProduct.getCreatedAt(), catalogProduct.getUpdatedAt());
    }

    public static List<CatalogProductDetailDto> toDtos(List<CatalogProduct> catalogProducts) {
        return catalogProducts.stream()
            .map(CatalogProductMapper::toDto)
            .toList();
    }

    public static CatalogProduct commandToDomain(CreateCatalogProductCommand command) {
        List<Category> categories = command.categories().stream()
            .map((category) -> Category.of(category))
            .toList();

        return CatalogProduct.create(command.sku(), command.sellerId(), command.name(), command.description(), command.price(), categories, command.imageUrl());
    }
}
