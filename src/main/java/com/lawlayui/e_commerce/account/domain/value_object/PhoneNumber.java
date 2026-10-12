package com.lawlayui.e_commerce.account.domain.value_object;

import com.lawlayui.e_commerce.account.domain.exception.InvalidPhoneNumberException;

public class PhoneNumber {
    private String value;

    private PhoneNumber(String phoneNumber) {
        this.value = phoneNumber;
    }

    public static PhoneNumber of(String phoneNumber) {

    String cleaned = phoneNumber.trim();

    if (cleaned.startsWith("08")) {
        cleaned = "+62 " + cleaned;
    } else if (cleaned.startsWith("+62") && !cleaned.startsWith("+62 ")) {
        cleaned = cleaned.replaceFirst("\\+62", "+62 ");
    }

    String phonePattern = "^\\+62\\s?08\\d{8,11}$";

    if (!cleaned.matches(phonePattern)) {
        throw new InvalidPhoneNumberException("Invalid phone number format: " + phoneNumber);
    }

    return new PhoneNumber(cleaned);
}

    public String getValue() {
        return value;
    }
}
