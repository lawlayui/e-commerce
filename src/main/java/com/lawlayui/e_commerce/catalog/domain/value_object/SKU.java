package com.lawlayui.e_commerce.catalog.domain.value_object;

public class SKU {
    private String value; 

    private SKU(String value) {
        this.value = value;
    }

    public static SKU of(String sku) {
        return new SKU(sku);
    }

    public String getValue() {
        return value;
    }
}
