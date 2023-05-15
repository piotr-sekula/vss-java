package com.virtuslab.vssjava.domain;

public interface PasswordRepository {

    void save(Password password);

    Password getByHash(String hash);
}
