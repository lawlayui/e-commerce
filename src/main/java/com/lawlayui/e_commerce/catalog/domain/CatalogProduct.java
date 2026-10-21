package com.lawlayui.e_commerce.catalog.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.lawlayui.e_commerce.catalog.domain.value_object.*;

public class CatalogProduct {
    private String catalogProductId; 
    private SKU sku;
    private String sellerId; 
    private String name; 
    private String description;
    private Money price;
    private List<Category> categories;
    private String imageUrl;
    private ProductStatus availability;
    private Rating avarageRating;
    private int reviewCount; 
    private LocalDateTime createdAt; 
    private LocalDateTime updatedAt;

    public void rename(String newName) {
        this.name = newName; 
    }

    public void changeDescription(String newDescription) {
        this.description = newDescription;
    }

    public void changePrice(BigDecimal newPrice) {
        this.price = Money.of(newPrice);
    }

    public void addCategory(String cateogry) {
        this.categories.add(Category.of(cateogry));
    }

    public void removeCategory(String category) {
        this.categories.remove(Category.of(category));
    }

    public void changeImageUrl(String newImageUrl) {
        this.imageUrl = newImageUrl;
    }

    public void markAsAvailable() {
        this.availability = ProductStatus.AVAILABLE;
    }
    
    public void markAsOutOfStock() {
        this.availability = ProductStatus.OUT_OF_STOCK;
    }

    public void discontinue() {
        this.availability = ProductStatus.DISCOUNTINUED;
    }

    public void changeRating(Double newRating) {
        this.avarageRating = Rating.of(newRating);
    }

    public void changeReviewCount(int newReviewCount) {
        this.reviewCount = newReviewCount;
    }

    private CatalogProduct(String catalogProductId, SKU sku, String sellerId, String name, String description, Money price, List<Category> categories
        , String imageUrl, ProductStatus availability, Rating avarageRating, int reviewCount, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        this.catalogProductId = catalogProductId;
        this.sku = sku;
        this.sellerId = sellerId;
        this.name = name; 
        this.description = description; 
        this.price = price; 
        this.categories = categories; 
        this.imageUrl = imageUrl; 
        this.availability = availability; 
        this.avarageRating = avarageRating; 
        this.reviewCount = reviewCount; 
        this.createdAt = createdAt; 
        this.updatedAt = updatedAt;
    }

    public static CatalogProduct create(String sku, String sellerId, String name, String description, BigDecimal price, 
        List<String> categories, String imageUr
    ) {
        List<Category> categories2 = categories.stream()
            .map(value -> Category.of(value))
            .toList();
        return new CatalogProduct(null, SKU.of(sku), sellerId, name, description, Money.of(price), categories2, imageUr, ProductStatus.DISCOUNTINUED, Rating.of(0.0), 0, LocalDateTime.now(), LocalDateTime.now());
    }

    public static CatalogProduct reconstitue(String catalogProductId, String sku, String sellerId, String name, String description, BigDecimal price, 
        List<String> categories, String imageUrl, ProductStatus availability, Double avargeRating, int reviewCount, LocalDateTime createdAt, LocalDateTime updatedAt
    ) {
        List<Category> categories2 = categories.stream()
            .map(value -> Category.of(value))
            .toList();
        return new CatalogProduct(catalogProductId, SKU.of(sku), sellerId, name, description, Money.of(price), categories2, imageUrl, availability, Rating.of(reviewCount), reviewCount, createdAt, updatedAt);
    }

    public String getCatalogProductId() {
        return catalogProductId;
    }
    public SKU getSku() {
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
    public ProductStatus getAvailability() {
        return availability;
    }
    public Rating getAvargeRating() {
        return avarageRating;
    }
    public int getReviewCount() {
        return reviewCount;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}