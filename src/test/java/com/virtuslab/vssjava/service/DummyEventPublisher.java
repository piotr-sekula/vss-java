package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.domain.EventPublisher;
import com.virtuslab.vssjava.events.PasswordSavedEvent;

import java.util.ArrayList;
import java.util.List;

class DummyEventPublisher implements EventPublisher {

    private final List<PasswordSavedEvent> events;

    DummyEventPublisher() {
        this.events = new ArrayList<>();
    }

    @Override
    public void publishEvent(PasswordSavedEvent event) {
        events.add(event);
    }

    List<PasswordSavedEvent> readAllEvents() {
        return events;
    }
}
