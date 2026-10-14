package com.lawlayui.e_commerce.account.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.lawlayui.e_commerce.account.application.port.out.EventPublisher;

@Service 
public class EventPublisherImpl implements EventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public EventPublisherImpl(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}
