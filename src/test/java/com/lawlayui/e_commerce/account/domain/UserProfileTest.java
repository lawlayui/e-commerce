package com.lawlayui.e_commerce.account.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.lawlayui.e_commerce.account.domain.entity.UserProfile;
import com.lawlayui.e_commerce.account.domain.exception.InvalidAddressException;
import com.lawlayui.e_commerce.account.domain.exception.InvalidPhoneNumberException;

class UserProfileTest {
    private static final String PROFILE_ID = "1";
    private static final String USER_ID = "2";
    private static final String FULL_NAME = "Lawlayui";
    private static final String PHONE_NUMBER = "0812345678";
    private static final String ADDRESS =
            "Jl. Merdeka No. 10, Kel. Braga, Kec. Sumur Bandung, Kota Bandung, Jawa Barat, 40111";

    private UserProfile userProfile;

    @BeforeEach
    void setUp() {
        userProfile = UserProfile.reconstitue(
                PROFILE_ID, USER_ID, FULL_NAME, PHONE_NUMBER, ADDRESS, LocalDateTime.now());
    }

    @Test
    void reconstitue_ShouldCreateUserProfile_WhenDataIsValid() {
        UserProfile profile = UserProfile.reconstitue(
                PROFILE_ID, USER_ID, FULL_NAME, PHONE_NUMBER, ADDRESS, LocalDateTime.now());

        assertNotNull(profile);
        assertEquals(PROFILE_ID, profile.getProfileId());
        assertEquals(USER_ID, profile.getUserId());
    }

    @Test
    void create_ShouldCreateUserProfile_WhenDataIsValid() {
        UserProfile profile = UserProfile.create(USER_ID, FULL_NAME, PHONE_NUMBER, ADDRESS);

        assertNotNull(profile);
        assertEquals(USER_ID, profile.getUserId());
        assertEquals(FULL_NAME, profile.getFullName());
        assertNotNull(profile.getUpdatedAt());
    }

    @Test
    void create_ShouldThrowInvalidPhoneNumberException_WhenPhoneNumberValueObjectDataIsInvalid() {
        assertThrows(
                InvalidPhoneNumberException.class,
                () -> UserProfile.create(USER_ID, FULL_NAME, "00654321", ADDRESS));
    }

    @Test
    void create_ShouldThrowInvalidAddressException_WhenAddressValueObjectDataIsInvalid() {
        assertThrows(
                InvalidAddressException.class,
                () -> UserProfile.create(USER_ID, FULL_NAME, PHONE_NUMBER, "Jl. Merdeka No. 45, Kel"));
    }

    @Test
    void rename_ShouldUpdateFullName_WhenNewNameIsProvided() {
        String newName = "Firure";

        userProfile.rename(newName);

        assertEquals(newName, userProfile.getFullName());
    }

    @Test
    void updateContactNumber_ShouldUpdatePhoneNumber_WhenNumberIsValid() {
        String newNumber = "0887654321";

        userProfile.updateContactNumber(newNumber);

        assertEquals("+62 " + newNumber, userProfile.getPhoneNumber().getValue());
    }

    @Test
    void updateContactNumber_ShouldThrowInvalidPhoneNumberException_WhenNumberIsInvalid() {
        assertThrows(
                InvalidPhoneNumberException.class,
                () -> userProfile.updateContactNumber("00654321"));
    }

    @Test
    void relocateTo_ShouldUpdateAddress_WhenAddressIsValid() {
        String newAddress =
                "Jl. Merdeka No. 10, Kel. Braga, Kec. Sumur Bandung, Kota Bandung, Jawa Barat, 40221";

        userProfile.relocateTo(newAddress);

        assertEquals(newAddress, userProfile.getAddress().getValue());
    }

    @Test
    void relocateTo_ShouldThrowInvalidAddressException_WhenAddressFormatIsInvalid() {
        assertThrows(
                InvalidAddressException.class,
                () -> userProfile.relocateTo("Jl. Merdeka No. 45, Kel"));
    }
}
