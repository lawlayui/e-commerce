package com.lawlayui.e_commerce.seller_product.application.services;

import java.math.BigDecimal;

import com.lawlayui.e_commerce.seller_product.application.exception.SellerProductNotFoundException;
import com.lawlayui.e_commerce.seller_product.application.port.out.SellerProductRepository;
import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public class UpdateProductPriceUseCaseImpl {
    private SellerProductRepository repository;

    public UpdateProductPriceUseCaseImpl(SellerProductRepository repository) {
        this.repository = repository;
    }

    public void updatePrice(String sellerProductId, BigDecimal newPrice) {
        SellerProduct sellerProduct = repository.getById(sellerProductId)
            .orElseThrow(() -> new SellerProductNotFoundException("Seller product with id " + sellerProductId + " not found"));

        sellerProduct.changePrice(newPrice);

        repository.save(sellerProduct);
    }
}
