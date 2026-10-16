package com.lawlayui.e_commerce.catalog.domain.value_object;

import com.lawlayui.e_commerce.catalog.domain.exception.RatingInvalidException;

public class Rating {
    private double value;

    private Rating(double value) {
        this.value = value;
    }

    private static void isValid(double rating) {
        if (rating < 0) {
            throw new RatingInvalidException("Rating cannot be negative");
        }
    }

    public static Rating of(double rating) {
        isValid(rating);
        return new Rating(rating);
    }

    public double getValue() {
        return value;
    }
}
