package com.virtuslab.vssjava.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.vssjava.controller.HashRequest;
import com.virtuslab.vssjava.domain.PasswordRepository;
import com.virtuslab.vssjava.support.KafkaSupport;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(initializers = {PasswordHashIntegrationTest.Initializer.class})
public class PasswordHashIntegrationTest extends AbstractIntegrationTest {

    private static final String TOPIC_NAME = "passwords";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private PasswordRepository passwordRepository;

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRES_CONTAINER.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRES_CONTAINER.getUsername(),
                    "spring.datasource.password=" + POSTGRES_CONTAINER.getPassword(),
                    "spring.kafka.producer.bootstrap-servers=" + KAFKA_CONTAINER.getBootstrapServers()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @BeforeEach
    void beforeEach() {
        KafkaSupport.createTopic(KAFKA_CONTAINER, TOPIC_NAME);
    }

    @AfterEach
    void afterEach() {
        KafkaSupport.resetKafka(KAFKA_CONTAINER);
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
    void shouldSavePasswordToDatabase() throws Exception {
        // given
        final var request = new HashRequest("MD5", "qwerty8()");

        // when
        this.mockMvc
                .perform(
                        post("/hash")
                                .content(asJsonString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // then
        final var passwordInDatabase = passwordRepository.getByHash("eb14221bc9f6b693cd74660328685a8f");
        Assertions.assertEquals("qwerty8()", passwordInDatabase.password());
        Assertions.assertEquals("MD5", passwordInDatabase.hashType());
    }

    @Test
    void shouldSendPasswordToKafka() throws Exception {
        // given
        final var request = new HashRequest("MD5", "123!@#abc");

        // when
        this.mockMvc
                .perform(
                        post("/hash")
                                .content(asJsonString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        // then
        var consumer = KafkaSupport.createKafkaConsumer(KAFKA_CONTAINER);
        consumer.subscribe(Collections.singletonList(TOPIC_NAME));

        assertEventIsInQueue(consumer);

        consumer.unsubscribe();
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


    private static void assertEventIsInQueue(KafkaConsumer<String, String> consumer) {
        final var startTime = System.currentTimeMillis();
        final var timeoutInMillis = 10 * 1000; // 10 seconds

        var eventFound = false;
        while (System.currentTimeMillis() - startTime < timeoutInMillis) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

            if (!records.isEmpty()) {
                assertThat(records.records(TOPIC_NAME))
                        .hasSize(1)
                        .extracting(ConsumerRecord::topic, ConsumerRecord::key, ConsumerRecord::value)
                        .containsExactly(tuple(TOPIC_NAME, null, "{\"hashType\":\"MD5\",\"password\":\"123!@#abc\",\"hash\":\"452933f447fe6aaa46cae4860239ca72\"}"));
                eventFound = true;
                break;
            }
        }
        if (!eventFound) {
            throw new RuntimeException("event not found in the queue");
        }
    }
}