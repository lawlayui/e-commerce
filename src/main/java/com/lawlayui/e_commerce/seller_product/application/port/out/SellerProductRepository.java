package com.lawlayui.e_commerce.seller_product.application.port.out;

import java.util.List;
import java.util.Optional;

import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public interface SellerProductRepository {
    void save(SellerProduct sellerProduct);
    void deleteById(String id);
    Optional<SellerProduct> getById(String sellerProductId);
    List<SellerProduct> getBySellerId(int page, int size, String sellerId);
}
