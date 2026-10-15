package com.lawlayui.e_commerce.infrastructure.services;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.lawlayui.e_commerce.account.application.port.in.command.RegisterUserOauthCommand;
import com.lawlayui.e_commerce.account.application.services.command.RegisterUserOauthUseCaseImpl;

public class CustomOAuth2UserService extends DefaultOAuth2UserService{
    private RegisterUserOauthUseCaseImpl registerUserOauthUseCaseImpl;

    public CustomOAuth2UserService(RegisterUserOauthUseCaseImpl registerUserOauthUseCaseImpl) {
        this.registerUserOauthUseCaseImpl = registerUserOauthUseCaseImpl;
    }

    @Override 
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email = oAuth2User.getAttribute("email");
        
        registerUserOauthUseCaseImpl.registerUser(new RegisterUserOauthCommand(email));

        return oAuth2User;
    }
}
