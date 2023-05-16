package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.controller.HashRequest;
import com.virtuslab.vssjava.events.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HashServiceTest {

    private HashService service;
    private DummyPasswordRepository repository;

    // expected hashes taken from: https://www.md5hashgenerator.com/
    public static Stream<Arguments> passwordsFixture() {
        return Stream.of(
                Arguments.of("P@ssw0rd!", "MD5", "8a24367a1f46c141048752f2d5bbd14b"),
                Arguments.of("P@ssw0rd!", "SHA-1", "076d3e6c4b9f654b5b220b9045b7458ab6b4cbc6"),
                Arguments.of("abcDEF123$%^", "MD5", "c1eeaa3da78c1687be9ddd471caa9103"),
                Arguments.of("abcDEF123$%^", "SHA-1", "d1d146fbf634ea022b02d9cd58fcf4d9aaff723b")
        );
    }

    @BeforeEach
    void setup() {
        repository = new DummyPasswordRepository();
        service = new HashService(repository, new Producer());
    }

    @ParameterizedTest
    @MethodSource("passwordsFixture")
    void shouldCalculateHashOfPassword(String password, String hashType, String expected) {
        // given
        HashRequest request = new HashRequest(hashType, password);

        // when
        final var hashResponse = service.calculatePasswordHash(request);

        // then
        assertEquals(expected, hashResponse.hash());
        assertEquals(password, hashResponse.password());
        assertEquals(hashType, hashResponse.hashType());
    }

    @Test
    void shouldStoreHashInDatabase() {
        // given
        HashRequest request = new HashRequest("MD5", "abc@@");

        // when
        service.calculatePasswordHash(request);

        // then
        final var found = repository.getByHash("2e15e23d6f2361d82a13ae589b360462");
        assertEquals("abc@@", found.password());
        assertEquals("MD5", found.hashType());
    }

    @Test
    void shouldThrowExceptionForInvalidHashType() {
        // given
        HashRequest request = new HashRequest("invalid-type", "password");

        // when
        IllegalArgumentException thrown =
                assertThrows(IllegalArgumentException.class, () -> service.calculatePasswordHash(request));

        assertEquals("java.security.NoSuchAlgorithmException: invalid-type MessageDigest not available", thrown.getMessage());
    }

}