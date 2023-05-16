package com.virtuslab.vssjava.integration;

import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractIntegrationTest {

    static final PostgreSQLContainer POSTGRES_CONTAINER;
    static final KafkaContainer KAFKA_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer("postgres:14.1")
                .withDatabaseName("integration-tests-db")
                .withUsername("test-user")
                .withPassword("test-pwd");

        KAFKA_CONTAINER = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.3.2"));

        POSTGRES_CONTAINER.start();
        KAFKA_CONTAINER.start();
    }
}
