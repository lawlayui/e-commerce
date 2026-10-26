package com.lawlayui.e_commerce.seller_product.application.port.in;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SellerProductDetailDto(
    String sellerProductId, 
    String sellerId, 
    String name, 
    String description, 
    List<String> categories, 
    BigDecimal price,
    String imageUrl, 
    String sku, 
    String status, 
    boolean isPublished,
    LocalDateTime createdAt, 
    LocalDateTime updatedAt
) {}
