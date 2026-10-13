package com.lawlayui.e_commerce.account.application.services.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.lawlayui.e_commerce.account.application.exception.UserNotFoundException;
import com.lawlayui.e_commerce.account.application.exception.UserProfileNotFoundException;
import com.lawlayui.e_commerce.account.application.port.in.query.GetAllUserQuery;
import com.lawlayui.e_commerce.account.application.port.out.UserProfileRepository;
import com.lawlayui.e_commerce.account.application.port.out.UserRepository;
import com.lawlayui.e_commerce.account.domain.User;
import com.lawlayui.e_commerce.account.domain.entity.UserProfile;
import com.lawlayui.e_commerce.account.domain.value_object.Email;
import com.lawlayui.e_commerce.account.domain.value_object.Role;

class UserApplicationQueryTest {
    private static final String USER_ID = "user-1";
    private static final String PROFILE_ID = "profile-1";

    @Test
    void getAll_shouldCallGetAll_whenQueryIsProvided() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        GetAllUserUseCaseImpl useCase = new GetAllUserUseCaseImpl(userRepository);
        GetAllUserQuery query = new GetAllUserQuery(2, 10);

        List<?> result = useCase.getAll(query);

        assertEquals(List.of(), result);
        assertEquals(1, userRepository.getAllCalls);
        assertEquals(2, userRepository.lastPage);
        assertEquals(10, userRepository.lastPageSize);
    }

    @Test
    void getById_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        GetUserByIdUseCaseImpl useCase = new GetUserByIdUseCaseImpl(userRepository);

        assertThrows(UserNotFoundException.class, () -> useCase.getById(USER_ID));

        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
    }

    @Test
    void getUserDetail_shouldCallBothRepositories_whenUserExists() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        RecordingUserProfileRepository userProfileRepository = new RecordingUserProfileRepository();
        User user = mock(User.class);
        Email email = mock(Email.class);
        when(user.getUserId()).thenReturn(USER_ID);
        when(user.getEmail()).thenReturn(email);
        when(email.getValue()).thenReturn("user@example.com");
        when(user.getRole()).thenReturn(Role.CUSTOMER);
        when(user.isActive()).thenReturn(true);
        when(user.getCreatedAt()).thenReturn(LocalDateTime.MIN);
        when(user.getUpdatedAt()).thenReturn(LocalDateTime.MIN);
        userRepository.user = Optional.of(user);
        GetUserDetailUseCaseImpl useCase =
                new GetUserDetailUseCaseImpl(userRepository, userProfileRepository);

        assertNotNull(useCase.getUserDetail(USER_ID));

        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
        assertEquals(1, userProfileRepository.getByUserIdCalls);
        assertEquals(USER_ID, userProfileRepository.lastUserId);
    }

    @Test
    void getUserDetail_shouldThrowUserNotFoundException_whenUserDoesNotExist() {
        RecordingUserRepository userRepository = new RecordingUserRepository();
        RecordingUserProfileRepository userProfileRepository = new RecordingUserProfileRepository();
        GetUserDetailUseCaseImpl useCase =
                new GetUserDetailUseCaseImpl(userRepository, userProfileRepository);

        assertThrows(UserNotFoundException.class, () -> useCase.getUserDetail(USER_ID));

        assertEquals(1, userRepository.getByIdCalls);
        assertEquals(USER_ID, userRepository.lastUserId);
        assertEquals(0, userProfileRepository.getByUserIdCalls);
    }

    @Test
    void getById_shouldThrowUserProfileNotFoundException_whenProfileDoesNotExist() {
        RecordingUserProfileRepository userProfileRepository =
                new RecordingUserProfileRepository();
        GetUserProfileByIdUseCaseImpl useCase =
                new GetUserProfileByIdUseCaseImpl(userProfileRepository);

        assertThrows(UserProfileNotFoundException.class, () -> useCase.getById(PROFILE_ID));

        assertEquals(1, userProfileRepository.getByIdCalls);
        assertEquals(PROFILE_ID, userProfileRepository.lastProfileId);
    }

    @Test
    void getByUserProfileIdAndUserId_shouldThrowUserProfileNotFoundException_whenProfileDoesNotExist() {
        RecordingUserProfileRepository userProfileRepository =
                new RecordingUserProfileRepository();
        GetUserProfileByUserProfileIdAndUserIdUseCaseImpl useCase =
                new GetUserProfileByUserProfileIdAndUserIdUseCaseImpl(userProfileRepository);

        assertThrows(
                UserProfileNotFoundException.class,
                () -> useCase.getByUserProfileIdAndUserId(PROFILE_ID, USER_ID));

        assertEquals(1, userProfileRepository.getByUserProfileIdAndUserIdCalls);
        assertEquals(PROFILE_ID, userProfileRepository.lastProfileId);
        assertEquals(USER_ID, userProfileRepository.lastUserId);
    }

    private static class RecordingUserRepository implements UserRepository {
        private Optional<User> user = Optional.empty();
        private int getByIdCalls;
        private int getAllCalls;
        private String lastUserId;
        private int lastPage;
        private int lastPageSize;

        @Override
        public void save(User user) {}

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
            getAllCalls++;
            lastPage = page;
            lastPageSize = pageSize;
            return List.of();
        }

        @Override
        public Optional<User> getByEmail(String email) {
            return Optional.empty();
        }
    }

    private static class RecordingUserProfileRepository implements UserProfileRepository {
        private int getByIdCalls;
        private int getByUserIdCalls;
        private int getByUserProfileIdAndUserIdCalls;
        private String lastProfileId;
        private String lastUserId;

        @Override
        public void save(UserProfile userProfile, User user) {}

        @Override
        public void deleteByUserProfileIdAndUserId(String userProfileId, String userId) {}

        @Override
        public Optional<UserProfile> getById(String userProfileId) {
            getByIdCalls++;
            lastProfileId = userProfileId;
            return Optional.empty();
        }

        @Override
        public List<UserProfile> getByUserId(String userId) {
            getByUserIdCalls++;
            lastUserId = userId;
            return List.of();
        }

        @Override
        public Optional<UserProfile> getByUserProfileIdAndUserId(
                String userProfileId, String userId) {
            getByUserProfileIdAndUserIdCalls++;
            lastProfileId = userProfileId;
            lastUserId = userId;
            return Optional.empty();
        }
    }
}
