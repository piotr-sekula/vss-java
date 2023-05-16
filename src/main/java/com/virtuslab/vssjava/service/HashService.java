package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.controller.HashRequest;
import com.virtuslab.vssjava.domain.EventPublisher;
import com.virtuslab.vssjava.domain.Password;
import com.virtuslab.vssjava.domain.PasswordRepository;
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

        final var digest = DigestUtils
                .getDigest(request.hashType())
                .digest(request.password().getBytes());

        final var password = new Password(
                request.hashType(),
                request.password(),
                HexUtils.toHexString(digest)
        );

        passwordRepository.save(password);
        eventPublisher.publishEvent(password);
        return password;
    }
}
