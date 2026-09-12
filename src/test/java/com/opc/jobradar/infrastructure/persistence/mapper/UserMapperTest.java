package com.opc.jobradar.infrastructure.persistence.mapper;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    @Test
    void shouldMapUserToEntity() {
        User user = new User();

        user.setId(1L);
        user.setName("Jaime");
        user.setEmail("jaime@email.com");
        user.setPassword("hashed-password");
        user.setRole(UserRole.USER);

        UserMapper mapper = new UserMapper();

        UserEntity entity = mapper.toEntity(user);

        assertEquals(1L, entity.getId());
        assertEquals("Jaime", entity.getName());
        assertEquals("jaime@email.com", entity.getEmail());
        assertEquals("hashed-password", entity.getPassword());
        assertEquals(UserRole.USER, entity.getRole());
    }

    @Test
    void shouldMapEntityToUser() {
        UserEntity entity = new UserEntity();

        entity.setId(1L);
        entity.setName("Jaime");
        entity.setEmail("jaime@email.com");
        entity.setPassword("hashed-password");
        entity.setRole(UserRole.USER);

        UserMapper mapper = new UserMapper();

        User user = mapper.toDomain(entity);

        assertEquals(1L, user.getId());
        assertEquals("Jaime", user.getName());
        assertEquals("jaime@email.com", user.getEmail());
        assertEquals("hashed-password", user.getPassword());
        assertEquals(UserRole.USER, user.getRole());
    }
}