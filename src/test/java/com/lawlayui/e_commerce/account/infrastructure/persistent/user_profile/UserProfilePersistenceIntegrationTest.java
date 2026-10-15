package com.lawlayui.e_commerce.account.infrastructure.persistent.user_profile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import com.lawlayui.e_commerce.account.domain.User;
import com.lawlayui.e_commerce.account.domain.entity.UserProfile;
import com.lawlayui.e_commerce.account.domain.value_object.Role;
import com.lawlayui.e_commerce.account.infrastructure.persistent.user.UserEntityJpa;
import com.lawlayui.e_commerce.account.infrastructure.persistent.user.UserEntityJpaRepository;

@DataJpaTest 
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UserProfileRepositoryImpl.class)
class UserProfilePersistenceIntegrationTest {
    private static final String FULL_NAME = "Integration Test User";
    private static final String PHONE_NUMBER = "0812345678";
    private static final String ADDRESS =
            "Jl. Merdeka No. 10, Kel. Braga, Kec. Sumur Bandung, Kota Bandung, Jawa Barat, 40111";

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserEntityJpaRepository userJpaRepository;

    @Autowired
    private UserProfileJpaRepository userProfileJpaRepository;

    @Autowired
    private UserProfileRepositoryImpl userProfileRepository;

    @Test
    void shouldConnectToConfiguredMySqlDatabase() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertTrue(connection.isValid(2), "The configured datasource should provide a valid connection");
            assertTrue(
                    connection.getMetaData().getURL().startsWith("jdbc:mysql:"),
                    "The integration test must use the configured MySQL datasource");
            assertTrue(
                    connection.getMetaData().getDatabaseProductName().toLowerCase().contains("mysql"),
                    "The connected database should identify itself as MySQL");
        }
    }

    @Test
    void shouldPersistReadUpdateAndDeleteProfileThroughCustomRepository() {
        User user = User.register("profile-" + UUID.randomUUID() + "@example.test", null, Role.CUSTOMER);
        LocalDateTime now = LocalDateTime.now();
        userJpaRepository.saveAndFlush(UserEntityJpa.builder()
                .userId(user.getUserId())
                .fullName(FULL_NAME)
                .email(user.getEmail().getValue())
                .hashedPassword(null)
                .role(user.getRole().name())
                .isActive(true)
                .createdAt(now)
                .updatedAt(now)
                .build());

        UserProfile profile = UserProfile.create(user.getUserId(), FULL_NAME, PHONE_NUMBER, ADDRESS);
        userProfileRepository.save(profile, user);
        userProfileJpaRepository.flush();

        List<UserProfile> profilesForUser = userProfileRepository.getByUserId(user.getUserId());
        assertEquals(1, profilesForUser.size());
        UserProfile created = profilesForUser.get(0);
        assertNotNull(created.getProfileId(), "The persistence mapper should assign a profile ID");
        assertEquals(user.getUserId(), created.getUserId());
        assertEquals(FULL_NAME, created.getFullName());
        assertEquals("+62 " + PHONE_NUMBER, created.getPhoneNumber().getValue());
        assertEquals(ADDRESS, created.getAddress().getValue());

        UserProfile loaded = userProfileRepository.getById(created.getProfileId()).orElseThrow();
        assertEquals(created.getProfileId(), loaded.getProfileId());
        assertEquals(FULL_NAME, loaded.getFullName());

        loaded.rename("Updated Integration Test User");
        loaded.updateContactNumber("0887654321");
        String updatedAddress =
                "Jl. Merdeka No. 10, Kel. Braga, Kec. Sumur Bandung, Kota Bandung, Jawa Barat, 40221";
        loaded.relocateTo(updatedAddress);
        userProfileRepository.save(loaded, user);
        userProfileJpaRepository.flush();

        UserProfile updated = userProfileRepository.getById(created.getProfileId()).orElseThrow();
        assertEquals("Updated Integration Test User", updated.getFullName());
        assertEquals("+62 0887654321", updated.getPhoneNumber().getValue());
        assertEquals(updatedAddress, updated.getAddress().getValue());

        userProfileRepository.deleteByUserProfileIdAndUserId(created.getProfileId(), user.getUserId());
        userProfileJpaRepository.flush();

        assertTrue(userProfileRepository.getById(created.getProfileId()).isEmpty());
        assertFalse(userProfileRepository.getByUserId(user.getUserId()).stream()
                .anyMatch(remaining -> created.getProfileId().equals(remaining.getProfileId())));
    }
}
