package com.lawlayui.e_commerce.account.infrastructure.persistent.user_profile;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.lawlayui.e_commerce.account.application.port.out.UserProfileRepository;
import com.lawlayui.e_commerce.account.domain.User;
import com.lawlayui.e_commerce.account.domain.entity.UserProfile;
import com.lawlayui.e_commerce.account.infrastructure.persistent.user.UserPersistentMapper;

@Repository 
public class UserProfileRepositoryImpl implements UserProfileRepository{
    private UserProfileJpaRepository jpaRepository;

    public UserProfileRepositoryImpl(UserProfileJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void deleteByUserProfileIdAndUserId(String userProfileId, String userId) {
        jpaRepository.deleteByProfileIdAndUserId(userProfileId, userId);
    }

    @Override
    public Optional<UserProfile> getById(String userProfileId) {
        return jpaRepository.findById(userProfileId).map(UserProfilePersistentMapper::toDomain);
    }

    @Override
    public List<UserProfile> getByUserId(String userId) {
        return UserProfilePersistentMapper.toDomains(jpaRepository.findByUserId(userId));
    }

    @Override
    public Optional<UserProfile> getByUserProfileIdAndUserId(String userProfileId, String userId) {
        return jpaRepository.findByUserProfileIdAndUserId(userProfileId, userId).map(UserProfilePersistentMapper::toDomain);
    }

    public void save(UserProfile userProfile, User user) {
        jpaRepository.save(UserProfilePersistentMapper.toEntity(userProfile, UserPersistentMapper.toEntity(user)));
    }
}
