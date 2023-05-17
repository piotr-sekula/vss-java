package com.virtuslab.vssjava.domain;

import com.virtuslab.vssjava.events.PasswordSavedEvent;

public interface EventPublisher {

    void publishEvent(PasswordSavedEvent passwordSavedEvent);
}
