package com.lawlayui.e_commerce.catalog.domain.value_object;

import java.math.BigDecimal;

public class Money {
    private BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money of(BigDecimal money) {
        return new Money(money);
    }

    public BigDecimal getValue() {
        return value;
    }
}
