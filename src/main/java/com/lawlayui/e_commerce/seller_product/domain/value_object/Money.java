package com.lawlayui.e_commerce.seller_product.domain.value_object;

import java.math.BigDecimal;

import com.lawlayui.e_commerce.catalog.domain.exception.MoneyInvalidException;

public class Money {
    private BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    private static void isValid(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new MoneyInvalidException("Money cannot be negative");
        }
    }

    public static Money of(BigDecimal value) {
        isValid(value);
        return new Money(value);
    }

    public BigDecimal getValue() {
        return value;
    }
}
