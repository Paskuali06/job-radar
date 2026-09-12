package com.opc.jobradar.infrastructure.persistence.repository;

import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository repository;

    @Test
    void shouldPersistAndRetrieveUser() {
        UserEntity user = new UserEntity();

        user.setName("Jaime");
        user.setEmail("jaime@email.com");
        user.setPassword("password");
        user.setRole(UserRole.USER);

        UserEntity saved = repository.saveAndFlush(user);

        assertNotNull(saved.getId());
        assertEquals("Jaime", saved.getName());
        assertEquals("jaime@email.com", saved.getEmail());
        assertEquals("password", saved.getPassword());
        assertEquals(UserRole.USER, saved.getRole());
    }

    @Test
    void shouldRejectDuplicatedEmail() {
        UserEntity firstUser = new UserEntity();

        firstUser.setName("Jaime");
        firstUser.setEmail("jaime@email.com");
        firstUser.setPassword("password");
        firstUser.setRole(UserRole.USER);

        repository.saveAndFlush(firstUser);

        UserEntity secondUser = new UserEntity();

        secondUser.setName("Otro");
        secondUser.setEmail("jaime@email.com");
        secondUser.setPassword("password");
        secondUser.setRole(UserRole.USER);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> repository.saveAndFlush(secondUser)
        );
    }

    @Test
    void shouldFindUserByEmail() {
    UserEntity user = new UserEntity();

    user.setName("Jaime");
    user.setEmail("jaime@email.com");
    user.setPassword("hashed-password");
    user.setRole(UserRole.USER);

    repository.saveAndFlush(user);

    UserEntity foundUser =
            repository.findByEmail("jaime@email.com");

    assertEquals("Jaime", foundUser.getName());
    assertEquals("jaime@email.com", foundUser.getEmail());
    assertEquals("hashed-password", foundUser.getPassword());
    assertEquals(UserRole.USER, foundUser.getRole());
}
}