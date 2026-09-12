package com.opc.jobradar.infrastructure.persistence.adapter;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.port.out.UserRepository;
import com.opc.jobradar.infrastructure.persistence.entity.UserEntity;
import com.opc.jobradar.infrastructure.persistence.mapper.UserMapper;
import com.opc.jobradar.infrastructure.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Component;

/**
 * Persists users using JPA.
 */
@Component
public class UserPersistenceAdapter implements UserRepository {

    private final UserJpaRepository repository;
    private final UserMapper mapper;

    public UserPersistenceAdapter(
            UserJpaRepository repository,
            UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void save(User user) {
        UserEntity entity = mapper.toEntity(user);
        repository.save(entity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return repository.findByEmail(email) != null;
    }

    @Override
    public User findByEmail(String email) {
        UserEntity entity = repository.findByEmail(email);
        return mapper.toDomain(entity);
    }
}