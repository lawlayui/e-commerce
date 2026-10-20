package com.lawlayui.e_commerce.catalog.application.exception;

public class CatalogProductNotFoundException extends RuntimeException {
    public CatalogProductNotFoundException(String msg) {
        super(msg);
    }
}
