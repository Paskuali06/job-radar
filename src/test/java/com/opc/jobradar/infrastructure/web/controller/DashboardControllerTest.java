package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.DashboardResponse;
import com.opc.jobradar.application.service.GetDashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    private static final Long USER_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetDashboardService getDashboardService;

    private MockHttpSession authenticatedSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("USER_ID", USER_ID);
        return session;
    }

    @Test
    void shouldReturnDashboardForAuthenticatedUser() throws Exception {
        DashboardResponse response = new DashboardResponse(
                10L,
                6L,
                3L,
                1L
        );

        when(getDashboardService.getDashboard(USER_ID))
                .thenReturn(response);

        mockMvc.perform(get("/dashboard")
                        .session(authenticatedSession()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        "application/json"
                ))
                .andExpect(content().json("""
                        {
                            "totalOffers": 10,
                            "pendingOffers": 6,
                            "appliedOffers": 3,
                            "rejectedOffers": 1
                        }
                        """));

        verify(getDashboardService).getDashboard(USER_ID);
    }

    @Test
    void shouldUseUserIdFromSession() throws Exception {
        when(getDashboardService.getDashboard(USER_ID))
                .thenReturn(new DashboardResponse(
                        4L,
                        2L,
                        1L,
                        1L
                ));

        mockMvc.perform(get("/dashboard")
                        .session(authenticatedSession()))
                .andExpect(status().isOk());

        verify(getDashboardService).getDashboard(USER_ID);
    }

    @Test
    void shouldRejectRequestWithoutAuthenticatedUser()
            throws Exception {

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isUnauthorized());
    }
}