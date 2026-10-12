package com.lawlayui.e_commerce.account.infrastructure.persistent.user_profile;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntityJpa, String> {
    List<UserProfileEntityJpa> findByUser_Id(String userId);
    Optional<UserProfileEntityJpa> findByUserProfileIdAndUser_Id(String userProfileId, String userId);

    void deleteByProfileIdAndUser_Id(String profileId, String userId);
}
