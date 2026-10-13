package com.lawlayui.e_commerce.account.application.services.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.lawlayui.e_commerce.account.application.event.UserRegisteredAsSeller;
import com.lawlayui.e_commerce.account.application.exception.UserAlredyRegistedException;
import com.lawlayui.e_commerce.account.application.exception.UserNotFoundException;
import com.lawlayui.e_commerce.account.application.exception.UserProfileNotFoundException;
import com.lawlayui.e_commerce.account.application.port.in.command.CreateUserProfileCommand;
import com.lawlayui.e_commerce.account.application.port.in.command.RegisterUserOauthCommand;
import com.lawlayui.e_commerce.account.application.port.in.command.UpdateUserProfileCommand;
import com.lawlayui.e_commerce.account.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.account.application.port.out.UserProfileRepository;
import com.lawlayui.e_commerce.account.application.port.out.UserRepository;
import com.lawlayui.e_commerce.account.domain.User;
import com.lawlayui.e_commerce.account.domain.entity.UserProfile;

class UserApplicationCommandTest {
    private static final String USER_ID = "user-1";
    private static final String PROFILE_ID = "profile-1";
    private static final String EMAIL = "user@example.com";

    @Test
    void registerAsSeller_shouldSaveUserAndPublishEvent_whenUserExists() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        User user = mock(User.class);
        userRepository.user = Optional.of(user);
        RecordingEventPublisher publisher = new RecordingEventPublisher();
        RegisterAsSellerUseCaseImpl useCase =
                new RegisterAsSellerUseCaseImpl(publisher, userRepository);

        useCase.registerAsSeller(USER_ID);

        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
        assertSame(user, userRepository.savedUser);
        verify(user).registerAsSeller();
        assertEquals(1, publisher.publishCalls);
        assertEquals(new UserRegisteredAsSeller(USER_ID), publisher.publishedEvent);
    }

    @Test
    void registerAsSeller_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        RecordingEventPublisher publisher = new RecordingEventPublisher();
        RegisterAsSellerUseCaseImpl useCase =
                new RegisterAsSellerUseCaseImpl(publisher, userRepository);

        assertThrows(UserNotFoundException.class, () -> useCase.registerAsSeller(USER_ID));

        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
        assertEquals(0, userRepository.saveCalls);
        assertEquals(0, publisher.publishCalls);
    }

    @Test
    void createUserProfile_shouldSaveProfileWithUser_whenUserExists() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        User user = mock(User.class);
        userRepository.user = Optional.of(user);
        RecordingUserProfileRepository userProfileRepository =
                new RecordingUserProfileRepository();
        CreateUserProfileUseCaseImpl useCase =
                new CreateUserProfileUseCaseImpl(userProfileRepository, userRepository);
        CreateUserProfileCommand command = new CreateUserProfileCommand(
                USER_ID,
                "First",
                "Last",
                "0812345678",
                "+62",
                "Jl. Merdeka No. 10, Kel. Braga, Kec. Sumur Bandung, Kota Bandung, Jawa Barat, 40111");

        useCase.createUserProfile(command);

        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
        assertEquals(1, userProfileRepository.saveCalls);
        assertNotNull(userProfileRepository.savedProfile);
        assertSame(user, userProfileRepository.savedUser);
    }

    @Test
    void createUserProfile_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        RecordingUserProfileRepository userProfileRepository =
                new RecordingUserProfileRepository();
        CreateUserProfileUseCaseImpl useCase =
                new CreateUserProfileUseCaseImpl(userProfileRepository, userRepository);
        CreateUserProfileCommand command = new CreateUserProfileCommand(
                USER_ID,
                "First",
                "Last",
                "0812345678",
                "+62",
                "Jl. Merdeka No. 10, Kel. Braga, Kec. Sumur Bandung, Kota Bandung, Jawa Barat, 40111");

        assertThrows(UserNotFoundException.class, () -> useCase.createUserProfile(command));

        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
        assertEquals(0, userProfileRepository.saveCalls);
    }

    @Test
    void registerUser_shouldThrowUserAlredyRegistedException_whenEmailIsAlreadyRegistered() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        userRepository.registeredEmail = true;
        RegisterUserOauthUseCaseImpl useCase = new RegisterUserOauthUseCaseImpl(userRepository);
        RegisterUserOauthCommand command = new RegisterUserOauthCommand(EMAIL);

        assertThrows(UserAlredyRegistedException.class, () -> useCase.registerUser(command));

        assertEquals(1, userRepository.getByEmailCalls);
        assertEquals(EMAIL, userRepository.lastEmail);
        assertEquals(0, userRepository.saveCalls);
    }

    @Test
    void updateUserProfile_shouldUpdateAndSaveProfile_whenProfileAndUserExist() {
        RecordingUserProfileRepository userProfileRepository =
                new RecordingUserProfileRepository();
        UserProfile userProfile = mock(UserProfile.class);
        userProfileRepository.userProfile = Optional.of(userProfile);
        RecordingUserRepository userRepository = new RecordingUserRepository();
        User user = mock(User.class);
        userRepository.user = Optional.of(user);
        UpdateUserProfileUseCaseImpl useCase =
                new UpdateUserProfileUseCaseImpl(userProfileRepository, userRepository);
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                PROFILE_ID, USER_ID, "New Name", "1234567890", null, null, null);

        useCase.updateUserProfile(command);

        assertEquals(1, userProfileRepository.getByUserProfileIdAndUserIdCalls);
        assertEquals(PROFILE_ID, userProfileRepository.lastProfileId);
        assertEquals(USER_ID, userProfileRepository.lastUserId);
        verify(userProfile).rename("New Name");
        verify(userProfile).updateContactNumber("1234567890");
        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
        assertEquals(1, userProfileRepository.saveCalls);
        assertSame(userProfile, userProfileRepository.savedProfile);
        assertSame(user, userProfileRepository.savedUser);
    }

    @Test
    void updateUserProfile_shouldThrowUserProfileNotFoundException_whenProfileDoesNotExist() {
        RecordingUserProfileRepository userProfileRepository =
                new RecordingUserProfileRepository();
        RecordingUserRepository userRepository = new RecordingUserRepository();
        UpdateUserProfileUseCaseImpl useCase =
                new UpdateUserProfileUseCaseImpl(userProfileRepository, userRepository);
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                PROFILE_ID, USER_ID, "New Name", null, null, null, null);

        assertThrows(UserProfileNotFoundException.class, () -> useCase.updateUserProfile(command));

        assertEquals(1, userProfileRepository.getByUserProfileIdAndUserIdCalls);
        assertEquals(PROFILE_ID, userProfileRepository.lastProfileId);
        assertEquals(USER_ID, userProfileRepository.lastUserId);
        assertEquals(0, userRepository.getByIdCalls);
        assertEquals(0, userProfileRepository.saveCalls);
    }

    @Test
    void updateUserProfile_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        RecordingUserProfileRepository userProfileRepository =
                new RecordingUserProfileRepository();
        UserProfile userProfile = mock(UserProfile.class);
        userProfileRepository.userProfile = Optional.of(userProfile);
        RecordingUserRepository userRepository = new RecordingUserRepository();
        UpdateUserProfileUseCaseImpl useCase =
                new UpdateUserProfileUseCaseImpl(userProfileRepository, userRepository);
        UpdateUserProfileCommand command = new UpdateUserProfileCommand(
                PROFILE_ID, USER_ID, "New Name", null, null, null, null);

        assertThrows(UserNotFoundException.class, () -> useCase.updateUserProfile(command));

        assertEquals(1, userProfileRepository.getByUserProfileIdAndUserIdCalls);
        verify(userProfile).rename("New Name");
        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
        assertEquals(0, userProfileRepository.saveCalls);
    }

    private static class RecordingUserRepository implements UserRepository {
        private Optional<User> user = Optional.empty();
        private boolean registeredEmail;
        private int getByIdCalls;
        private int getByEmailCalls;
        private int saveCalls;
        private String lastUserId;
        private String lastEmail;
        private User savedUser;

        @Override
        public void save(User user) {
            saveCalls++;
            savedUser = user;
        }

        @Override
        public void deleteById(String userId) {}

        @Override
        public Optional<User> getById(String userId) {
            getByIdCalls++;
            lastUserId = userId;
            return user;
        }

        @Override
        public List<User> getAll(int page, int pageSize) {
            return List.of();
        }

        @Override
        public Optional<User> getByEmail(String email) {
            getByEmailCalls++;
            lastEmail = email;
            return registeredEmail ? Optional.of(mock(User.class)) : Optional.empty();
        }
    }

    private static class RecordingUserProfileRepository implements UserProfileRepository {
        private Optional<UserProfile> userProfile = Optional.empty();
        private int getByUserProfileIdAndUserIdCalls;
        private int saveCalls;
        private String lastProfileId;
        private String lastUserId;
        private UserProfile savedProfile;
        private User savedUser;

        @Override
        public void save(UserProfile userProfile, User user) {
            saveCalls++;
            savedProfile = userProfile;
            savedUser = user;
        }

        @Override
        public void deleteByUserProfileIdAndUserId(String userProfileId, String userId) {}

        @Override
        public Optional<UserProfile> getById(String userProfileId) {
            return Optional.empty();
        }

        @Override
        public List<UserProfile> getByUserId(String userId) {
            return List.of();
        }

        @Override
        public Optional<UserProfile> getByUserProfileIdAndUserId(
                String userProfileId, String userId) {
            getByUserProfileIdAndUserIdCalls++;
            lastProfileId = userProfileId;
            lastUserId = userId;
            return userProfile;
        }
    }

    private static class RecordingEventPublisher implements EventPublisher {
        private int publishCalls;
        private Object publishedEvent;

        @Override
        public void publish(Object event) {
            publishCalls++;
            publishedEvent = event;
        }
    }
}
