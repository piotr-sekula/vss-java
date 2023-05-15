package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.controller.HashRequest;
import com.virtuslab.vssjava.domain.Password;
import com.virtuslab.vssjava.domain.PasswordRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    private final PasswordRepository passwordRepository;

    public HashService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
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
        return password;
    }
}
