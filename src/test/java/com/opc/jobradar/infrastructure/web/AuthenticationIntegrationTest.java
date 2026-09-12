package com.opc.jobradar.infrastructure.web;

import com.opc.jobradar.application.service.LoginUserService;
import com.opc.jobradar.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginUserService loginUserService;

    @Test
    void shouldRejectProtectedEndpointWithoutAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/job-offers"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowProtectedEndpointWithAuthenticatedUser() throws Exception {
        mockMvc.perform(
                get("/job-offers")
                        .session(sessionWithUser())
        ).andExpect(status().isOk());
    }

    @Test
    void shouldAllowLoginWithoutAuthenticatedUser() throws Exception {
        User user = new User();
        user.setId(1L);

        when(loginUserService.login("test@example.com", "password"))
                .thenReturn(user);

        mockMvc.perform(post("/auth/login")
                        .param("email", "test@example.com")
                        .param("password", "password"))
                .andExpect(status().isOk());

        verify(loginUserService).login("test@example.com", "password");
    }

    private MockHttpSession sessionWithUser() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("USER_ID", 1L);
        return session;
    }
}