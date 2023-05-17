package com.virtuslab.vssjava.events;

import com.virtuslab.vssjava.domain.Password;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordSerializerTest {

    @Test
    void shouldSerializePassword() {
        // given
        Password password = new Password("MD5", "abc", "hash123");

        // when
        final var result = PasswordSerializer.serializePassword(PasswordSavedEvent.fromPassword(password));

        // then
        assertEquals("{\"hashType\":\"MD5\",\"password\":\"abc\",\"hash\":\"hash123\"}", result);
    }
}