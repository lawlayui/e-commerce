package com.lawlayui.e_commerce.catalog.infrastructure.event;

import org.springframework.context.ApplicationEventPublisher;

import com.lawlayui.e_commerce.account.application.port.out.EventPublisher;

public class EventPublisherImpl implements EventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public EventPublisherImpl(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override 
    public void publish(Object event) {
        eventPublisher.publishEvent(event);
    }
}
