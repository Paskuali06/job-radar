package com.opc.jobradar.infrastructure.web;

import com.opc.jobradar.application.service.LoginUserService;
import com.opc.jobradar.domain.model.User;
import com.opc.jobradar.application.service.GetAuthenticatedUserService;
import com.opc.jobradar.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LoginUserService loginUserService;

    @MockitoBean
    private GetAuthenticatedUserService getAuthenticatedUserService;

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

        mockMvc.perform(
                post("/auth/login")
                        .param("email", "test@example.com")
                        .param("password", "password")
        ).andExpect(status().isOk());

        verify(loginUserService).login("test@example.com", "password");
    }

    @Test
    void shouldRejectProtectedEndpointAfterLogout() throws Exception {
        MockHttpSession session = sessionWithUser();

        mockMvc.perform(
                post("/auth/logout")
                        .session(session)
        ).andExpect(status().isOk());

        mockMvc.perform(
                get("/job-offers")
                        .session(session)
        ).andExpect(status().isUnauthorized());
    }

    private MockHttpSession sessionWithUser() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("USER_ID", 1L);
        return session;
    }
    @Test
    void shouldRejectAccessToAnotherUsersJobOffer() throws Exception {
    mockMvc.perform(
            get("/job-offers/999")
                    .session(sessionWithUser())
    ).andExpect(status().isNotFound());
}

    @Test
    void shouldRejectUpdateOfAnotherUsersJobOffer() throws Exception {
    mockMvc.perform(
            put("/job-offers/999")
                    .session(sessionWithUser())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                            {
                                "company": "Other Company",
                                "title": "Other Job",
                                "url": "https://example.com/job"
                            }
                            """)
    ).andExpect(status().isNotFound());
}
    @Test
    void shouldReturnAuthenticatedUserWithoutPassword() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setName("Jaime");
        user.setEmail("jaime@test.com");
        user.setPassword("secret");
        user.setRole(UserRole.USER);

        when(getAuthenticatedUserService.getUser(1L))
                .thenReturn(user);

        mockMvc.perform(
                        get("/auth/me")
                                .session(sessionWithUser())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jaime"))
                .andExpect(jsonPath("$.email").value("jaime@test.com"))
                .andExpect(jsonPath("$.role").value("USER"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}