package com.lawlayui.e_commerce.account.application.services.command;

import com.lawlayui.e_commerce.account.application.exception.UserNotFoundException;
import com.lawlayui.e_commerce.account.application.exception.UserProfileNotFoundException;
import com.lawlayui.e_commerce.account.application.mapper.UserProfileMapper;
import com.lawlayui.e_commerce.account.application.port.in.command.UpdateUserProfileCommand;
import com.lawlayui.e_commerce.account.application.port.out.UserProfileRepository;
import com.lawlayui.e_commerce.account.application.port.out.UserRepository;
import com.lawlayui.e_commerce.account.domain.entity.UserProfile;

public class UpdateUserProfileUseCaseImpl {
    private UserProfileRepository repository; 
    private UserRepository userRepository;

    public UpdateUserProfileUseCaseImpl(UserProfileRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public void updateUserProfile(UpdateUserProfileCommand command) {
        UserProfile userProfile = repository.getByUserProfileIdAndUserId(command.getUserProfileId(), command.getUserId())
            .orElseThrow(() -> new UserProfileNotFoundException(command.getUserProfileId(), command.getUserId()));

        UserProfileMapper.updateUserProfile(userProfile, command);       

        repository.save(userProfile, userRepository.getById(command.getUserId()).orElseThrow(() -> new UserNotFoundException(command.getUserId())));
    }
}
