package com.lawlayui.e_commerce.catalog.application.port.in;

import java.math.BigDecimal;

public record CatalogProductDto(
    String id, 
    String name,
    String availabiltiy, 
    BigDecimal price,
    String imageUrl, 
    Double rating, 
    int reviewCount  
) {}
