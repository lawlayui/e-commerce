package com.lawlayui.e_commerce.catalog.application.port.in;

import java.math.BigDecimal;

public record SearchProductQuery(
    int page, 
    int size, 
    String name, 
    String category, 
    BigDecimal startingPrice,
    BigDecimal finalPrice, 
    Double rating
) {}
