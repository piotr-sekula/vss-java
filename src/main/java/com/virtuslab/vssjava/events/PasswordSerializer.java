package com.virtuslab.vssjava.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.vssjava.domain.Password;

class PasswordSerializer {

    static String serializePassword(Password password) {
        ObjectMapper mapper = new ObjectMapper();
        String json;

        try {
            json = mapper.writeValueAsString(password);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return json;
    }
}
