package com.lawlayui.e_commerce.catalog.domain.exception;

public class MoneyInvalidException extends RuntimeException{
    public MoneyInvalidException(String msg) {
        super(msg);
    }
}
