package com.lawlayui.e_commerce.catalog.infrastructure.persistent;

import java.util.List;
import java.util.UUID;

import com.lawlayui.e_commerce.account.domain.User;
import com.lawlayui.e_commerce.catalog.domain.CatalogProduct;
import com.lawlayui.e_commerce.catalog.domain.value_object.ProductStatus;

public class CatalogProductPersistentMapper {
    public static CatalogProduct toDomain(CatalogProductJpa catalogProductJpa) {
        return CatalogProduct.reconstitue(catalogProductJpa.getId(), catalogProductJpa.getSku(), catalogProductJpa.getSeller().getUserId(), catalogProductJpa.getName(), catalogProductJpa.getDescription(), catalogProductJpa.getPrice(), catalogProductJpa.getCategories(), catalogProductJpa.getImageUrl(), catalogProductJpa.getAvailability() == "AVAILABLE" ? ProductStatus.AVAILABLE : ProductStatus.OUT_OF_STOCK, catalogProductJpa.getAvarageRating(), catalogProductJpa.getReviewCount(), catalogProductJpa.getCreatedAt(), catalogProductJpa.getUpdatedAt());
    }

    public static List<CatalogProduct> toDomains(List<CatalogProductJpa> catalogProductJpas) {
        return catalogProductJpas.stream()  
            .map(CatalogProductPersistentMapper::toDomain) 
            .toList();
    }

    public static CatalogProductJpa toEntity(CatalogProduct catalogProduct, User seller) {
        return CatalogProductJpa.builder()
        .id(catalogProduct == null ? UUID.randomUUID().toString() : catalogProduct.getCatalogProductId())
        .seller(seller)
        .sku(catalogProduct.getSku().getValue())
        .name(catalogProduct.getName())
        .description(catalogProduct.getDescription())
        .availability(catalogProduct.getAvailability().name())
        .avarageRating(catalogProduct.getAvargeRating().getValue())
        .categories(catalogProduct.getCategories().stream().map((category) -> category.getCategory()).toList())
        .imageUrl(catalogProduct.getImageUrl())
        .price(catalogProduct.getPrice().getValue())
        .reviewCount(catalogProduct.getReviewCount())
        .createdAt(catalogProduct.getCreatedAt())
        .updatedAt(catalogProduct.getUpdatedAt())
        .build();
    }
}
