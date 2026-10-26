package com.lawlayui.e_commerce.seller_product.application.services;

import com.lawlayui.e_commerce.seller_product.application.exception.SellerProductNotFoundException;
import com.lawlayui.e_commerce.seller_product.application.port.in.SellerProductDetailDto;
import com.lawlayui.e_commerce.seller_product.application.port.mapper.SellerProductMapper;
import com.lawlayui.e_commerce.seller_product.application.port.out.SellerProductRepository;
import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public class GetProductDetailByIdUseCaseImpl {
    private SellerProductRepository repository;

    public GetProductDetailByIdUseCaseImpl(SellerProductRepository repository) {
        this.repository = repository;
    }

    public SellerProductDetailDto getProductDetailById(String sellerProductId) {
        SellerProduct sellerProduct = repository.getById(sellerProductId)
            .orElseThrow(() -> new SellerProductNotFoundException("Seller product with id " + sellerProductId + " not found"));

        return SellerProductMapper.toDetailDto(sellerProduct);
    }
}
