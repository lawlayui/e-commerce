package com.lawlayui.e_commerce.seller_product.application.event;

public record ProductPublishedEvent(
    String sellerProductId, 
    String sellerId,
    String sku
) {}
