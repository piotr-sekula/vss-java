package com.virtuslab.vssjava.events;

import com.virtuslab.vssjava.domain.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventPublisher implements EventPublisher {

    private static final String TOPIC = "passwords";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishEvent(PasswordSavedEvent passwordSavedEvent) {
        kafkaTemplate.send(TOPIC, PasswordSerializer.serializePassword(passwordSavedEvent));
    }
}
