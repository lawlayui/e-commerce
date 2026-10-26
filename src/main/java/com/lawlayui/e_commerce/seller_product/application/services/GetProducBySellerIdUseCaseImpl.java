package com.lawlayui.e_commerce.seller_product.application.services;

import java.util.List;

import com.lawlayui.e_commerce.seller_product.application.port.in.SellerProductDto;
import com.lawlayui.e_commerce.seller_product.application.port.mapper.SellerProductMapper;
import com.lawlayui.e_commerce.seller_product.application.port.out.SellerProductRepository;

public class GetProducBySellerIdUseCaseImpl {
    private SellerProductRepository repository;

    public GetProducBySellerIdUseCaseImpl(SellerProductRepository repository) {
        this.repository = repository;
    }

    public List<SellerProductDto> getBySellerId(int page, int size, String sellerId) {
        return repository.getBySellerId(page, size, sellerId).stream()
            .map(SellerProductMapper::toDto)
            .toList();
    }
}