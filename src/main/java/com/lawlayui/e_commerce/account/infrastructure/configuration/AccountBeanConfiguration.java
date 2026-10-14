package com.lawlayui.e_commerce.account.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lawlayui.e_commerce.account.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.account.application.port.out.UserProfileRepository;
import com.lawlayui.e_commerce.account.application.port.out.UserRepository;
import com.lawlayui.e_commerce.account.application.services.query.*;
import com.lawlayui.e_commerce.account.application.services.command.*;

@Configuration 
public class AccountBeanConfiguration {
    @Bean 
    public GetAllUserUseCaseImpl getAllUsersUseCase(UserRepository userRepository) {
        return new GetAllUserUseCaseImpl(userRepository);
    }

    @Bean 
    public GetUserByIdUseCaseImpl getUserByIdUseCase(UserRepository userRepository) {
        return new GetUserByIdUseCaseImpl(userRepository);
    }

    @Bean 
    public GetUserDetailUseCaseImpl getUserDetailUseCase(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        return new GetUserDetailUseCaseImpl(userRepository, userProfileRepository);
    }
    
    @Bean 
    public GetUserProfileByIdUseCaseImpl getUserProfileUseCase(UserProfileRepository userProfileRepository) {
        return new GetUserProfileByIdUseCaseImpl(userProfileRepository);
    }

    @Bean 
    public GetUserProfileByUserProfileIdAndUserIdUseCaseImpl getUserProfileByUserIdUseCase(UserProfileRepository userProfileRepository) {
        return new GetUserProfileByUserProfileIdAndUserIdUseCaseImpl(userProfileRepository);
    }

    @Bean 
    public CreateUserProfileUseCaseImpl createUserProfileUseCase(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        return new CreateUserProfileUseCaseImpl(userProfileRepository, userRepository);
    }

    @Bean 
    public UpdateUserProfileUseCaseImpl updateUserProfileUseCase(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        return new UpdateUserProfileUseCaseImpl(userProfileRepository, userRepository);
    }

    @Bean 
    public RegisterAsSellerUseCaseImpl registerAsSellerUseCase(UserRepository userRepository, EventPublisher publisher) {
        return new RegisterAsSellerUseCaseImpl(publisher, userRepository);
    }

    @Bean 
    public RegisterUserOauthUseCaseImpl registerUserOAuthUseCase(UserRepository userRepository, EventPublisher publisher) {
        return new RegisterUserOauthUseCaseImpl(userRepository);
    }
}
