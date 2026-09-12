package com.opc.jobradar.infrastructure.persistence.adapter;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.model.UserRole;
import com.opc.jobradar.infrastructure.persistence.entity.UserEntity;
import com.opc.jobradar.infrastructure.persistence.mapper.UserMapper;
import com.opc.jobradar.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class UserPersistenceAdapterTest {

    @Test
    void shouldSaveUser() {
        UserJpaRepository repository = mock(UserJpaRepository.class);
        UserMapper mapper = new UserMapper();

        UserPersistenceAdapter adapter =
                new UserPersistenceAdapter(repository, mapper);

        User user = new User();

        user.setName("Jaime");
        user.setEmail("jaime@email.com");
        user.setPassword("hashed-password");
        user.setRole(UserRole.USER);

        adapter.save(user);

        verify(repository).save(argThat(entity ->
                entity.getName().equals("Jaime")
                        && entity.getEmail().equals("jaime@email.com")
                        && entity.getPassword().equals("hashed-password")
                        && entity.getRole() == UserRole.USER
        ));
    }
    @Test
    void shouldFindUserByEmail() {
    UserJpaRepository repository = mock(UserJpaRepository.class);
    UserMapper mapper = new UserMapper();

    UserPersistenceAdapter adapter =
            new UserPersistenceAdapter(repository, mapper);

    UserEntity entity = new UserEntity();

    entity.setId(1L);
    entity.setName("Jaime");
    entity.setEmail("jaime@email.com");
    entity.setPassword("hashed-password");
    entity.setRole(UserRole.USER);

    when(repository.findByEmail("jaime@email.com"))
            .thenReturn(entity);

    User user = adapter.findByEmail("jaime@email.com");

    assertEquals(1L, user.getId());
    assertEquals("Jaime", user.getName());
    assertEquals("jaime@email.com", user.getEmail());
    assertEquals("hashed-password", user.getPassword());
    assertEquals(UserRole.USER, user.getRole());
}
}