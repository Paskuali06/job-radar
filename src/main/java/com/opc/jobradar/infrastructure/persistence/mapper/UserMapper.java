package com.opc.jobradar.infrastructure.persistence.mapper;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.infrastructure.persistence.entity.UserEntity;

/**
 * Maps between User domain objects and UserEntity persistence objects.
 */
public class UserMapper {

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();

        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());

        return entity;
    }

    public User toDomain(UserEntity entity) {
        User user = new User();

        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setRole(entity.getRole());

        return user;
    }
}