package com.lawlayui.e_commerce.catalog.domain.value_object;

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

    public static Money of(BigDecimal money) {
        isValid(money);
        return new Money(money);
    }

    public BigDecimal getValue() {
        return value;
    }
}
