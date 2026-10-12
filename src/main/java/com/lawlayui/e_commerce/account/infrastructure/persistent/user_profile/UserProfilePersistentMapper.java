package com.lawlayui.e_commerce.account.infrastructure.persistent.user_profile;

import java.util.List;
import java.util.UUID;

import com.lawlayui.e_commerce.account.domain.entity.UserProfile;
import com.lawlayui.e_commerce.account.infrastructure.persistent.user.UserEntityJpa;

public class UserProfilePersistentMapper{
    public static UserProfileEntityJpa toEntity(UserProfile userProfile, UserEntityJpa user) {
        return UserProfileEntityJpa.builder()
                .profileId(userProfile.getProfileId() == null ? UUID.randomUUID().toString() : userProfile.getProfileId())
                .user(user)
                .fullName(userProfile.getFullName())
                .phoneNumber(userProfile.getPhoneNumber().getValue())
                .address(userProfile.getAddress().getValue())
                .build();
    }

    public static UserProfile toDomain(UserProfileEntityJpa entity) {
        return UserProfile.reconstitue(
            entity.getProfileId(),
            entity.getUser().getUserId(),
            entity.getFullName(),
            entity.getPhoneNumber(),
            entity.getAddress(),
            entity.getUpdatedAt()
        );
    }

    public static List<UserProfile> toDomains(List<UserProfileEntityJpa> entity) {
        return entity.stream()
            .map(UserProfilePersistentMapper::toDomain)
            .toList();
    }
}
