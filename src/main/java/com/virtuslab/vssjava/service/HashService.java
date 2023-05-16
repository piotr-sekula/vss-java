package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.controller.HashRequest;
import com.virtuslab.vssjava.domain.Password;
import com.virtuslab.vssjava.domain.PasswordRepository;
import com.virtuslab.vssjava.events.Producer;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    private final PasswordRepository passwordRepository;
    private final Producer eventsProducer;

    public HashService(PasswordRepository passwordRepository, Producer eventsProducer) {
        this.passwordRepository = passwordRepository;
        this.eventsProducer = eventsProducer;
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
        eventsProducer.sendMessage(password);
        return password;
    }
}
