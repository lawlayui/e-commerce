package com.lawlayui.e_commerce.account.infrastructure.persistent.user_profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface UserProfileJpaRepository extends JpaRepository<UserProfileEntityJpa, String> {

    @Query("SELECT p FROM UserProfileEntityJpa p WHERE p.user.userId = :userId")
    List<UserProfileEntityJpa> findByUserId(@Param("userId") String userId);

    @Query("SELECT p FROM UserProfileEntityJpa p WHERE p.profileId = :userProfileId AND p.user.userId = :userId")
    Optional<UserProfileEntityJpa> findByUserProfileIdAndUserId(
        @Param("userProfileId") String userProfileId, 
        @Param("userId") String userId
    );

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("DELETE FROM UserProfileEntityJpa p WHERE p.profileId = :profileId AND p.user.userId = :userId")
    void deleteByProfileIdAndUserId(
        @Param("profileId") String profileId, 
        @Param("userId") String userId
    );
}