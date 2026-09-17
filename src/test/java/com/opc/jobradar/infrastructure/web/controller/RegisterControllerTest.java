package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.RegisterUserService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class RegisterControllerTest {

    @Test
    void shouldRegisterUser() throws Exception {
        RegisterUserService registerUserService =
                org.mockito.Mockito.mock(RegisterUserService.class);

        doNothing()
                .when(registerUserService)
                .register(
                        "Jaime",
                        "jaime@email.com",
                        "password"
                );

        RegisterController controller =
                new RegisterController(registerUserService);

        MockMvc mockMvc =
                standaloneSetup(controller).build();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Jaime",
                                          "email": "jaime@email.com",
                                          "password": "password"
                                        }
                                        """)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotReturnPassword() throws Exception {
        RegisterUserService registerUserService =
                org.mockito.Mockito.mock(RegisterUserService.class);

        RegisterController controller =
                new RegisterController(registerUserService);

        MockMvc mockMvc =
                standaloneSetup(controller).build();

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                          "name": "Jaime",
                                          "email": "jaime@email.com",
                                          "password": "password"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        result -> org.junit.jupiter.api.Assertions.assertFalse(
                                result.getResponse()
                                        .getContentAsString()
                                        .contains("password")
                        )
                );
    }
}