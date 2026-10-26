package com.lawlayui.e_commerce.seller_product.application.services;

import com.lawlayui.e_commerce.seller_product.application.port.in.CreateProductCommand;
import com.lawlayui.e_commerce.seller_product.application.port.mapper.SellerProductMapper;
import com.lawlayui.e_commerce.seller_product.application.port.out.SellerProductRepository;
import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public class CreateProductUseCaseImpl {
    private SellerProductRepository repository;
    
    public CreateProductUseCaseImpl(SellerProductRepository repository) {
        this.repository = repository;
    }

    public void create(CreateProductCommand command) {
        SellerProduct sellerProduct = SellerProductMapper.commandToSellerProduct(command);

        repository.save(sellerProduct);
    }
}
