package com.virtuslab.vssjava.integration;

import org.testcontainers.containers.PostgreSQLContainer;

public abstract class AbstractDatabaseTest {

    static final PostgreSQLContainer POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer("postgres:14.1")
                .withDatabaseName("integration-tests-db")
                .withUsername("test-user")
                .withPassword("test-pwd");

        POSTGRES_CONTAINER.start();
    }
}
