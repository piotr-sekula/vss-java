package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.controller.HashRequest;
import com.virtuslab.vssjava.view.HashResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    public HashResponse calculatePasswordHash(HashRequest request) {

        final var digest1 = DigestUtils
                .getDigest(request.hashType())
                .digest(request.password().getBytes());

        return new HashResponse(
                request.hashType(),
                request.password(),
                HexUtils.toHexString(digest1)
        );
    }
}
