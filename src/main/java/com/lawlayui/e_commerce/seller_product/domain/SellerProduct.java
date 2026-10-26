package com.lawlayui.e_commerce.seller_product.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.lawlayui.e_commerce.seller_product.domain.exception.InvalidAddCategory;
import com.lawlayui.e_commerce.seller_product.domain.exception.InvalidChangeDescription;
import com.lawlayui.e_commerce.seller_product.domain.exception.InvalidRename;
import com.lawlayui.e_commerce.seller_product.domain.value_object.Category;
import com.lawlayui.e_commerce.seller_product.domain.value_object.Money;
import com.lawlayui.e_commerce.seller_product.domain.value_object.ProductStatus;

public class SellerProduct {
    private String sellerProductId; 
    private String sku; 
    private String sellerId; 
    private String name; 
    private String description;
    private Money price; 
    private List<Category> categories;
    private String imageUrl; 
    private boolean isPublished; 
    private ProductStatus status; 
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt; 

    public static SellerProduct reconstitue(
        String sellerProductId, 
        String sku, 
        String sellerId, 
        String name, 
        String description, 
        BigDecimal price, 
        List<String> categories, 
        String imageUrl, 
        boolean isPublished, 
        ProductStatus status, 
        LocalDateTime createdAt, 
        LocalDateTime udpatedAt
    ) {
        return new SellerProduct(
            sellerProductId, 
            sku, 
            sellerId, 
            name, 
            description, 
            Money.of(price), 
            categories.stream().map((category) -> Category.of(category)).toList(), 
            imageUrl, 
            isPublished, 
            status, 
            createdAt, 
            udpatedAt);
    }

    public static SellerProduct create(String sellerId, String name, String description, BigDecimal price, List<String> categories,
        String imageUrl
    ) {
        return new SellerProduct(sellerId, 
            UUID.randomUUID().toString(),
            sellerId,
            name,
            description, 
            Money.of(price), 
            categories.stream().map((category) -> Category.of(category)).toList(), 
            imageUrl, 
            false, 
            ProductStatus.DISCONTINUED, 
            LocalDateTime.now(), 
            LocalDateTime.now());
    }

    private SellerProduct(String sellerProductId, String sku, String sellerId, String name, String description, 
        Money price, List<Category> categories, String imageUrl, boolean isPublished, ProductStatus status, 
        LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.sellerProductId = sellerProductId;
        this.sku = sku; 
        this.sellerId = sellerId;
        this.name = name; 
        this.description = description; 
        this.price = price; 
        this.categories = categories;
        this.imageUrl = imageUrl;
        this.isPublished = isPublished;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getSellerProductId() {
        return sellerProductId;
    }
    public String getSku() {
        return sku;
    }
    public String getSellerId() {
        return sellerId;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Money getPrice() {
        return price;
    }
    public List<Category> getCategories() {
        return categories;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public boolean isPublished() {
        return isPublished;
    }
    public ProductStatus getStatus() {
        return status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void rename(String newName) {
        if (newName == null || newName.length() < 3 || newName.length() > 255) {
            throw new InvalidRename("Name must be between 3 and 255 characters.");
        }
        this.name = newName;
        this.updatedAt = LocalDateTime.now();
    }

    public void changeDescription(String newDescription) {
        if (newDescription == null || newDescription.length() < 50 || newDescription.length() > 500) {
            throw new InvalidChangeDescription("Description must be between 50 and 500 characters.");
        }
        this.description = newDescription;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePrice(BigDecimal newPrice) {
        this.price = Money.of(newPrice);
        this.updatedAt = LocalDateTime.now();
    }

    public void addCategory(String newCategory) {
        boolean exists = this.categories.stream()
            .anyMatch(c -> c.getValue().equals(newCategory));
        if (exists) {
            throw new InvalidAddCategory("Category already exists.");
        }
        
        List<Category> updatedCategories = new ArrayList<>(this.categories);
        updatedCategories.add(Category.of(newCategory));
        this.categories = updatedCategories;
        
        this.updatedAt = LocalDateTime.now();
    }

    public void changeImageUrl(String newUrl) {
        this.imageUrl = newUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public void publish() {
        this.isPublished = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void unPublish() {
        this.isPublished = false;
        this.updatedAt = LocalDateTime.now();
    }
}
