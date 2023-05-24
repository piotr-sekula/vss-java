package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.controller.HashRequest;
import com.virtuslab.vssjava.domain.EventPublisher;
import com.virtuslab.vssjava.domain.Password;
import com.virtuslab.vssjava.domain.PasswordRepository;
import com.virtuslab.vssjava.events.PasswordSavedEvent;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    private final PasswordRepository passwordRepository;
    private final EventPublisher eventPublisher;

    public HashService(PasswordRepository passwordRepository, EventPublisher eventPublisher) {
        this.passwordRepository = passwordRepository;
        this.eventPublisher = eventPublisher;
    }

    public Password calculatePasswordHash(HashRequest request) {
        Password password;

        try {
            final var digest = DigestUtils
                    .getDigest(request.hashType())
                    .digest(request.password().getBytes());

            password = new Password(
                    request.hashType(),
                    request.password(),
                    HexUtils.toHexString(digest)
            );
        } catch (IllegalArgumentException e) {
            throw new UnknownAlgorithmException("Unknown hashing algorithm: " + request.hashType());
        }


        passwordRepository.save(password);
        eventPublisher.publishEvent(PasswordSavedEvent.fromPassword(password));
        return password;
    }
}
