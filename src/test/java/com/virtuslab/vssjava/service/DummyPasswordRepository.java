package com.virtuslab.vssjava.service;

import com.virtuslab.vssjava.domain.Password;
import com.virtuslab.vssjava.domain.PasswordRepository;

import java.util.ArrayList;
import java.util.List;

class DummyPasswordRepository implements PasswordRepository {

    private final List<Password> data;

    public DummyPasswordRepository() {
        this.data = new ArrayList<>();
    }

    @Override
    public void save(Password entity) {
        data.add(entity);
    }

    @Override
    public Password getByHash(String hash) {
        return data.stream()
                .filter(p -> p.hash().equals(hash))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Hash not found in database"));
    }
}
