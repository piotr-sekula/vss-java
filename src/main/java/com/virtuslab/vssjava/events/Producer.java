package com.virtuslab.vssjava.events;

import com.virtuslab.vssjava.domain.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private static final String TOPIC = "passwords";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(Password message) {
        kafkaTemplate.send(TOPIC, PasswordSerializer.serializePassword(message));
    }
}
