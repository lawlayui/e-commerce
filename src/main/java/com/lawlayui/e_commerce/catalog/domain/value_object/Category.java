package com.lawlayui.e_commerce.catalog.domain.value_object;

public class Category {
    private String value;

    private Category(String value) {
        this.value = value;
    }

    public static Category of(String category) {
        return new Category(category);
    }

    public String getCategory() {
        return value;
    }
}
