package com.lawlayui.e_commerce.seller_product.application.event;

public record ProductUnPublishedEvent(
    String sellerProductId, 
    String sellerId, 
    String sku
) {}
