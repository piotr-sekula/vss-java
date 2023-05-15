package com.virtuslab.vssjava.repository;

import com.virtuslab.vssjava.domain.Password;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "hashed_passwords")
class PasswordEntity {

    private PasswordEntity(String password, String hashType, String passwordHash) {
        this.password = password;
        this.hashType = hashType;
        this.passwordHash = passwordHash;
    }

    static PasswordEntity from(Password password) {
        return new PasswordEntity(password.password(), password.hashType(), password.hash());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String password;

    @Column(name = "hash_type")
    private String hashType;

    @Column(name = "password_hash")
    private String passwordHash;

    public String getPassword() {
        return password;
    }

    public String getHashType() {
        return hashType;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
