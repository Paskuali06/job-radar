package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.domain.port.out.UserRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GetAuthenticatedUserServiceTest {

    private final UserRepository userRepository =
            mock(UserRepository.class);

    private final GetAuthenticatedUserService service =
            new GetAuthenticatedUserService(userRepository);

    @Test
    void shouldReturnUserById() {
        User user = new User();
        user.setId(1L);
        user.setName("Jaime");

        when(userRepository.findById(1L))
                .thenReturn(user);

        User result = service.getUser(1L);

        assertEquals(1L, result.getId());
        assertEquals("Jaime", result.getName());

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldRejectNullUserId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> service.getUser(null)
        );

        verifyNoInteractions(userRepository);
    }
}