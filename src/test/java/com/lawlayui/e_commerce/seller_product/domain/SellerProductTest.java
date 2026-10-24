package com.lawlayui.e_commerce.seller_product.domain;

import com.lawlayui.e_commerce.seller_product.domain.exception.InvalidAddCategory;
import com.lawlayui.e_commerce.seller_product.domain.exception.InvalidChangeDescription;
import com.lawlayui.e_commerce.seller_product.domain.exception.InvalidRename;
import com.lawlayui.e_commerce.seller_product.domain.value_object.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SellerProductTest {

    private SellerProduct sellerProduct;

    @BeforeEach
    void setUp() {
        sellerProduct = SellerProduct.create(
                "SKU123",
                "sellerId1",
                "Valid Product Name",
                "This is a valid product description that is long enough to pass validation.",
                new BigDecimal("100.00"),
                List.of("Electronics"),
                "http://image.url"
        );
    }

    @Test
    void rename_ValidName_UpdatesName() {
        sellerProduct.rename("New Valid Name");
        assertEquals("New Valid Name", sellerProduct.getName());
    }

    @Test
    void rename_ShortName_ThrowsInvalidRename() {
        assertThrows(InvalidRename.class, () -> sellerProduct.rename("AB"));
    }

    @Test
    void rename_LongName_ThrowsInvalidRename() {
        String longName = "A".repeat(256);
        assertThrows(InvalidRename.class, () -> sellerProduct.rename(longName));
    }

    @Test
    void changeDescription_ValidDescription_UpdatesDescription() {
        String validDescription = "This is a new valid product description that is definitely more than 50 characters long.";
        sellerProduct.changeDescription(validDescription);
        assertEquals(validDescription, sellerProduct.getDescription());
    }

    @Test
    void changeDescription_ShortDescription_ThrowsInvalidChangeDescription() {
        assertThrows(InvalidChangeDescription.class, () -> sellerProduct.changeDescription("Too short"));
    }

    @Test
    void changeDescription_LongDescription_ThrowsInvalidChangeDescription() {
        String longDescription = "A".repeat(501);
        assertThrows(InvalidChangeDescription.class, () -> sellerProduct.changeDescription(longDescription));
    }

    @Test
    void changePrice_ValidPrice_UpdatesPrice() {
        BigDecimal newPrice = new BigDecimal("200.00");
        sellerProduct.changePrice(newPrice);
        assertEquals(newPrice, sellerProduct.getPrice().getValue());
    }

    @Test
    void addCategory_NewCategory_UpdatesCategories() {
        sellerProduct.addCategory("Books");
        assertTrue(sellerProduct.getCategories().stream().anyMatch(c -> c.getValue().equals("Books")));
    }

    @Test
    void addCategory_DuplicateCategory_ThrowsInvalidAddCategory() {
        assertThrows(InvalidAddCategory.class, () -> sellerProduct.addCategory("Electronics"));
    }

    @Test
    void changeImageUrl_ValidUrl_UpdatesUrl() {
        String newUrl = "http://new.image.url";
        sellerProduct.changeImageUrl(newUrl);
        assertEquals(newUrl, sellerProduct.getImageUrl());
    }

    @Test
    void publish_ChangesToTrue() {
        sellerProduct.unPublish(); // Ensure it starts as false
        sellerProduct.publish();
        assertTrue(sellerProduct.isPublished());
    }

    @Test
    void unPublish_ChangesToFalse() {
        sellerProduct.publish(); // Ensure it starts as true
        sellerProduct.unPublish();
        assertFalse(sellerProduct.isPublished());
    }
}
