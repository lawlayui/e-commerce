package com.lawlayui.e_commerce.catalog.application.port.in;

import java.math.BigDecimal;
import java.util.List;

public record CreateCatalogProductCommand(
    String sku, 
    String sellerId, 
    String name, 
    String description, 
    BigDecimal price, 
    List<String> categories,
    String imageUrl 
) {}
