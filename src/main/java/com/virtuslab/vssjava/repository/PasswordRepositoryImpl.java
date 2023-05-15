package com.virtuslab.vssjava.repository;

import com.virtuslab.vssjava.domain.Password;
import com.virtuslab.vssjava.domain.PasswordRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PasswordRepositoryImpl implements PasswordRepository {

    private final PasswordEntityJpaRepository jpaRepository;

    public PasswordRepositoryImpl(PasswordEntityJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public void save(Password password) {
        PasswordEntity entity = PasswordEntity.from(password);
        jpaRepository.save(entity);
    }

    @Override
    public Password getByHash(String hash) {
        final PasswordEntity foundEntity = jpaRepository.findByPasswordHash(hash);
        return new Password(foundEntity.getHashType(), foundEntity.getPassword(), foundEntity.getPasswordHash());
    }
}
