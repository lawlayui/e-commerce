package com.lawlayui.e_commerce.seller_product.application.port.in;

import java.math.BigDecimal;
import java.util.List;

public record CreateProductCommand(
    String sellerId, 
    String name, 
    String description, 
    List<String> categories,
    String imageUrl,
    BigDecimal price
) {}
