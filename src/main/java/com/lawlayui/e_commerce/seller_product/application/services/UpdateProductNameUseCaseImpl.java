package com.lawlayui.e_commerce.seller_product.application.services;

import com.lawlayui.e_commerce.seller_product.application.exception.SellerProductNotFoundException;
import com.lawlayui.e_commerce.seller_product.application.port.out.SellerProductRepository;
import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public class UpdateProductNameUseCaseImpl {
    private SellerProductRepository repository;

    public UpdateProductNameUseCaseImpl(SellerProductRepository repository) {
        this.repository = repository;
    }

    public void udpateName(String sellerProductId, String newName) {
        SellerProduct sellerProduct = repository.getById(sellerProductId)
            .orElseThrow(() -> new SellerProductNotFoundException("Seller product with id " + sellerProductId + " not found"));

        sellerProduct.rename(newName);

        repository.save(sellerProduct);
    }
}
