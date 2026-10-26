package com.lawlayui.e_commerce.seller_product.application.port.mapper;

import com.lawlayui.e_commerce.seller_product.application.port.in.CreateProductCommand;
import com.lawlayui.e_commerce.seller_product.application.port.in.SellerProductDetailDto;
import com.lawlayui.e_commerce.seller_product.application.port.in.SellerProductDto;
import com.lawlayui.e_commerce.seller_product.domain.SellerProduct;

public class SellerProductMapper {
    public static SellerProduct commandToSellerProduct(CreateProductCommand command) {
        return SellerProduct.create(
            command.sellerId(),
            command.name(), 
            command.description(),
            command.price(), 
            command.categories(), 
            command.imageUrl()
        );
    }

    public static SellerProductDto toDto(SellerProduct sellerProduct) {
        return new SellerProductDto(
            sellerProduct.getSellerProductId(),
            sellerProduct.getName(),
            sellerProduct.getCategories().stream().map((category) -> category.getValue()).toList(), 
            sellerProduct.getImageUrl(), 
            sellerProduct.getPrice().getValue()
        );
    }

    public static SellerProductDetailDto toDetailDto(SellerProduct sellerProduct) {
        return new SellerProductDetailDto(
            sellerProduct.getSellerProductId(),
            sellerProduct.getSellerId(),
            sellerProduct.getName(), 
            sellerProduct.getDescription(), 
            sellerProduct.getCategories().stream().map((category) -> category.getValue()).toList(), 
            sellerProduct.getPrice().getValue(), 
            sellerProduct.getImageUrl(), 
            sellerProduct.getSku(), 
            sellerProduct.getStatus().name(), 
            sellerProduct.isPublished(), 
            sellerProduct.getCreatedAt(), 
            sellerProduct.getUpdatedAt()
        );
    }
}
