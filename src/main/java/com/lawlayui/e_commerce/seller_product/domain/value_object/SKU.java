package com.lawlayui.e_commerce.seller_product.domain.value_object;

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
