package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.domain.EventPublisher;
import com.virtuslab.vssjava.domain.Password;

import java.util.ArrayList;
import java.util.List;

class DummyEventPublisher implements EventPublisher {

    private final List<Password> events;

    DummyEventPublisher() {
        this.events = new ArrayList<>();
    }

    @Override
    public void publishEvent(Password password) {
        events.add(password);
    }

    List<Password> readAllEvents() {
        return events;
    }
}
