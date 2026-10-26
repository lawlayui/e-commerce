package com.lawlayui.e_commerce.seller_product.application.services;

import com.lawlayui.e_commerce.seller_product.application.event.ProductCreatedEvent;
import com.lawlayui.e_commerce.seller_product.application.port.in.CreateProductCommand;
import com.lawlayui.e_commerce.seller_product.application.port.mapper.SellerProductMapper;
import com.lawlayui.e_commerce.seller_product.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.seller_product.application.port.out.SellerProductRepository;
import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public class CreateProductUseCaseImpl {
    private SellerProductRepository repository;
    private EventPublisher eventPublisher;
    
    public CreateProductUseCaseImpl(SellerProductRepository repository, EventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public void create(CreateProductCommand command) {
        SellerProduct sellerProduct = SellerProductMapper.commandToSellerProduct(command);

        repository.save(sellerProduct);

        eventPublisher.publish(new ProductCreatedEvent(sellerProduct.getSellerProductId(), sellerProduct.getSellerId(), sellerProduct.getSku()));
    }
}
