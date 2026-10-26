package com.lawlayui.e_commerce.seller_product.application.services;

import com.lawlayui.e_commerce.seller_product.application.event.ProductPublishedEvent;
import com.lawlayui.e_commerce.seller_product.application.exception.SellerProductNotFoundException;
import com.lawlayui.e_commerce.seller_product.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.seller_product.application.port.out.SellerProductRepository;
import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public class PublishProductUseCaseImpl {
    private SellerProductRepository repository;
    private EventPublisher eventPublisher;

    public PublishProductUseCaseImpl(SellerProductRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public void publish(String sellerProductId) {
        SellerProduct sellerProduct = repository.getById(sellerProductId)
            .orElseThrow(() -> new SellerProductNotFoundException("Seller product with id " + sellerProductId + " not found"));

        sellerProduct.publish();

        repository.save(sellerProduct);

        eventPublisher.publish(new ProductPublishedEvent(sellerProductId, sellerProduct.getSellerId(), sellerProduct.getSku()));
    }
}
