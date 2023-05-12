package com.virtuslab.vssjava.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.vssjava.controller.HashRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
public class HashControllerIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

    }

    @Test
    void shouldReturnHardcodedTextFromDummyEndpoint() throws Exception {
        this.mockMvc
                .perform(get("/hash"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello Hash!"));
    }

    @Test
    void shouldReturnHashForPassword() throws Exception {
        final var request = new HashRequest("MD5", "a!b@c#");

        this.mockMvc
                .perform(
                        post("/hash")
                                .content(asJsonString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hashType").value("MD5"))
                .andExpect(jsonPath("$.password").value("a!b@c#"))
                .andExpect(jsonPath("$.hash").value("6ebd61d52ee810f87ac866c6bfa933d3"));
    }

    @Test
    void shouldReturnBadRequestForInvalidHashAlgorithm() throws Exception {
        final var request = new HashRequest("invalid", "a!b@c#");

        this.mockMvc
                .perform(
                        post("/hash")
                                .content(asJsonString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Unknown hashing algorithm: invalid"));
    }

    private static String asJsonString(final HashRequest request) {
        try {
            return new ObjectMapper().writeValueAsString(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}