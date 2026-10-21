package com.lawlayui.e_commerce.catalog.infrastructure.persistent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.lawlayui.e_commerce.account.domain.User;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "catalog_products") 
@AllArgsConstructor 
@NoArgsConstructor 
@Builder 
@Setter 
@Getter 
public class CatalogProductJpa {
    @Id 
    private String id; 

    @Column(unique = true, nullable = false)
    private String sku;

    @ManyToOne 
    @JoinColumn(name = "seller_id")
    private User seller; 

    @Column(nullable = false, length = 255)
    private String name; 

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @ElementCollection 
    @CollectionTable(name = "catalog_product_categories", joinColumns = @JoinColumn(name = "product_id"))
    @Column(nullable = false)
    private List<String> categories;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable =  false)
    private String availability;

    @Column(nullable = false, name = "avarage_rating")
    private Double avarageRating; 

    @Column(nullable = false, name = "review_count")
    private int reviewCount; 

    @Column(name = "created_at")
    @CreationTimestamp 
    private LocalDateTime createdAt; 

    @Column(name = "updated_at")
    @UpdateTimestamp 
    private LocalDateTime updatedAt;
}
