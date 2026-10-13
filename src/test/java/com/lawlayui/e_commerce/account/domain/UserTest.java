package com.lawlayui.e_commerce.account.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.lawlayui.e_commerce.account.domain.exception.InvalidEmailException;
import com.lawlayui.e_commerce.account.domain.exception.InvalidHashedPasswordException;
import com.lawlayui.e_commerce.account.domain.value_object.Email;
import com.lawlayui.e_commerce.account.domain.value_object.Password;
import com.lawlayui.e_commerce.account.domain.value_object.Role;

class UserTest {
    private static final String USER_ID = "1";
    private static final String RAW_EMAIL = "person@gmail.com";
    private static final String RAW_PASSWORD = "12345678";
    private static final Role ROLE = Role.CUSTOMER;
    private static final Password HASHED_PASSWORD =
            Password.createFromRaw(RAW_PASSWORD.toCharArray());

    private User user;

    @BeforeEach
    void setUp() {
        user = User.rehydrate(
                USER_ID,
                RAW_EMAIL,
                HASHED_PASSWORD.getHashedValue(),
                ROLE,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    @DisplayName("Registers a user with valid data")
    void register_returnsUser_whenDataIsValid() {
        User registered = User.register(RAW_EMAIL, RAW_PASSWORD, ROLE);

        assertNotNull(registered);
        assertNotNull(registered.getUserId());
        assertEquals(RAW_EMAIL, registered.getEmail().getValue());
        assertEquals(ROLE, registered.getRole());
        assertTrue(registered.isActive());
        assertTrue(registered.getHashedPassword().verify(RAW_PASSWORD.toCharArray()));
    }

    @Test
    @DisplayName("Rejects registration with an invalid email")
    void register_throwsInvalidEmailException_whenEmailIsInvalid() {
        assertThrows(
                InvalidEmailException.class,
                () -> User.register("invalidEmail", RAW_PASSWORD, ROLE));
    }

    @Test
    @DisplayName("Rehydrates a user with valid data")
    void rehydrate_returnsUser_whenDataIsValid() {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = createdAt.plusMinutes(1);

        User rehydrated = User.rehydrate(
                USER_ID,
                RAW_EMAIL,
                HASHED_PASSWORD.getHashedValue(),
                ROLE,
                createdAt,
                updatedAt);

        assertNotNull(rehydrated);
        assertEquals(USER_ID, rehydrated.getUserId());
        assertEquals(RAW_EMAIL, rehydrated.getEmail().getValue());
        assertEquals(ROLE, rehydrated.getRole());
        assertEquals(createdAt, rehydrated.getCreatedAt());
        assertEquals(updatedAt, rehydrated.getUpdatedAt());
    }

    @Test
    @DisplayName("Rejects rehydration with an invalid email")
    void rehydrate_throwsInvalidEmailException_whenEmailIsInvalid() {
        assertThrows(
                InvalidEmailException.class,
                () -> User.rehydrate(
                        USER_ID,
                        "invalidEmail",
                        HASHED_PASSWORD.getHashedValue(),
                        ROLE,
                        LocalDateTime.now(),
                        LocalDateTime.now()));
    }

    @Test
    @DisplayName("Rejects rehydration with an invalid hashed password")
    void rehydrate_throwsInvalidHashedPasswordException_whenPasswordIsInvalid() {
        assertThrows(
                InvalidHashedPasswordException.class,
                () -> User.rehydrate(
                        USER_ID,
                        RAW_EMAIL,
                        "invalidFormatPassword",
                        ROLE,
                        LocalDateTime.now(),
                        LocalDateTime.now()));
    }

    @Test
    @DisplayName("Verifies matching and non-matching passwords")
    void passwordVerification_matchesExpectedPassword() {
        assertTrue(user.getHashedPassword().verify(RAW_PASSWORD.toCharArray()));
        assertFalse(user.getHashedPassword().verify("incorrect".toCharArray()));
    }

    @Test
    @DisplayName("Email value object normalizes valid addresses")
    void email_normalizesValidAddress() {
        assertEquals("person@gmail.com", Email.of("Person@Gmail.com").getValue());
    }

    @Test
    @DisplayName("Email value object rejects invalid address data")
    void email_rejectsInvalidValidationData() {
        String[] invalidEmails = {"invalidEmail", "person@", "@gmail.com", "person@gmail"};

        for (String invalidEmail : invalidEmails) {
            assertThrows(InvalidEmailException.class, () -> Email.of(invalidEmail));
        }
    }

    @Test
    @DisplayName("Password value object rejects invalid hashed password data")
    void password_rejectsInvalidHashedValidationData() {
        String[] invalidHashes = {null, "", "invalidFormatPassword"};

        for (String invalidHash : invalidHashes) {
            assertThrows(
                    InvalidHashedPasswordException.class,
                    () -> Password.createFromHashed(invalidHash));
        }
    }

    @Test
    @DisplayName("Resetting and changing a password replaces the stored password")
    void passwordChanges_updateStoredPassword() {
        String resetPassword = "new_password";
        user.resetPassword(resetPassword);
        assertTrue(user.getHashedPassword().verify(resetPassword.toCharArray()));

        String changedPassword = "new_password1";
        user.changePassword(changedPassword);
        assertTrue(user.getHashedPassword().verify(changedPassword.toCharArray()));
    }

    @Test
    @DisplayName("Registering as a seller changes the user's role")
    void registerAsSeller_changesRole() {
        user.registerAsSeller();

        assertEquals(Role.SELLER, user.getRole());
    }
}
