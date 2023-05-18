package com.virtuslab.vssjava.events;

import com.virtuslab.vssjava.domain.EventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventPublisher implements EventPublisher {

    @Value(value = "${spring.kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void publishEvent(PasswordSavedEvent passwordSavedEvent) {
        kafkaTemplate.send(topic, PasswordSerializer.serializePassword(passwordSavedEvent));
    }
}
