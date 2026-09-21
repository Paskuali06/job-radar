package com.opc.jobradar.infrastructure.web.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldExposeOpenApiDocumentation() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ))
                .andExpect(content().string(
                        containsString("\"openapi\"")
                ));
    }
    @Test
    void shouldExposeSwaggerUi() throws Exception {
    mockMvc.perform(get("/swagger-ui.html"))
            .andExpect(status().is3xxRedirection());
}
    @Test
    void shouldDocumentApplicationEndpoints() throws Exception {
    mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"/auth/login\"")))
            .andExpect(content().string(containsString("\"/auth/logout\"")))
            .andExpect(content().string(containsString("\"/auth/register\"")))
            .andExpect(content().string(containsString("\"/auth/me\"")))
            .andExpect(content().string(containsString("\"/job-offers\"")))
            .andExpect(content().string(containsString("\"/job-offers/{id}\"")));
}
}