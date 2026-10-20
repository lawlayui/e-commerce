package com.lawlayui.e_commerce.catalog.application.port.in;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CatalogProductDetailDto(
    String id, 
    String sku,
    String sellerId,
    String name, 
    String description, 
    BigDecimal price, 
    List<String> categories,
    String imageUrl, 
    String availability, 
    Double avargeRating, 
    int reviewCount, 
    LocalDateTime createdAt, 
    LocalDateTime updatedAt
) {}
