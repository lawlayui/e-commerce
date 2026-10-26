package com.lawlayui.e_commerce.seller_product.application.services;

import com.lawlayui.e_commerce.seller_product.application.exception.SellerProductNotFoundException;
import com.lawlayui.e_commerce.seller_product.application.port.out.SellerProductRepository;
import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public class UpdateProductDescriptionUseCaseImpl {
    private SellerProductRepository repository;

    public UpdateProductDescriptionUseCaseImpl(SellerProductRepository repository) {
        this.repository = repository;
    }

    public void updateDescription(String sellerProductId, String newDesc) {
        SellerProduct sellerProduct = repository.getById(sellerProductId)
            .orElseThrow(() -> new SellerProductNotFoundException("Seller product with id " + sellerProductId + " not found"));

        sellerProduct.changeDescription(newDesc);

        repository.save(sellerProduct);
    }
}