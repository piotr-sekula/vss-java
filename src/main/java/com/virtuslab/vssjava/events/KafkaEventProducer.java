package com.virtuslab.vssjava.events;

import com.virtuslab.vssjava.domain.EventPublisher;
import com.virtuslab.vssjava.domain.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventProducer implements EventPublisher {

    private static final String TOPIC = "passwords";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishEvent(Password password) {
        kafkaTemplate.send(TOPIC, PasswordSerializer.serializePassword(password));
    }
}
