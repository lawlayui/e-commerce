package com.lawlayui.e_commerce.seller_product.application.event;

public record ProductCreatedEvent(String sellerProductId, String sellerId, String sku) {}
