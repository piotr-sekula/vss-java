package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.controller.HashRequest;
import com.virtuslab.vssjava.view.HashResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.buf.HexUtils;

public class HashService {

    public HashResponse calculatePasswordHash(HashRequest request) {

        final var digest1 = DigestUtils
                .getDigest(request.getHashType())
                .digest(request.getPassword().getBytes());

        return new HashResponse(
                request.getHashType(),
                request.getPassword(),
                HexUtils.toHexString(digest1)
        );
    }
}
