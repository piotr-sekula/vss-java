package com.virtuslab.vssjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface PasswordEntityJpaRepository extends JpaRepository<PasswordEntity, UUID> {

    PasswordEntity findByPasswordHash(String passwordHash);
}
