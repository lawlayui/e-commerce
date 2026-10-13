package com.lawlayui.e_commerce.account.application.services.command;

import com.lawlayui.e_commerce.account.application.event.UserRegisteredAsSeller;
import com.lawlayui.e_commerce.account.application.exception.UserNotFoundException;
import com.lawlayui.e_commerce.account.application.port.out.EventPublisher;
import com.lawlayui.e_commerce.account.application.port.out.UserRepository;
import com.lawlayui.e_commerce.account.domain.User;

public class RegisterAsSellerUseCaseImpl {
    private UserRepository userRepository;
    private EventPublisher publisher;

    public RegisterAsSellerUseCaseImpl(EventPublisher publisher, UserRepository userRepository) {
        this.publisher = publisher;
        this.userRepository = userRepository;
    }

    public void registerAsSeller(String userId) {
        User user = userRepository.getById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        user.registerAsSeller();
        userRepository.save(user);
        publisher.publish(new UserRegisteredAsSeller(userId));
    }
}
