package com.opc.jobradar.application.service;

import com.opc.jobradar.domain.port.out.JobOfferRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GetDashboardServiceTest {

    private final JobOfferRepository repository = mock(JobOfferRepository.class);

    private final GetDashboardService service =
            new GetDashboardService(repository);

    @Test
    void shouldReturnDashboardMetricsForUser() {
        Long userId = 1L;

        when(repository.countByUserId(userId)).thenReturn(10L);
        when(repository.countByUserIdAndStatus(userId, "PENDIENTE"))
                .thenReturn(6L);
        when(repository.countByUserIdAndStatus(userId, "SOLICITADA"))
                .thenReturn(3L);
        when(repository.countByUserIdAndStatus(userId, "RECHAZADA"))
                .thenReturn(1L);

        DashboardResponse result = service.getDashboard(userId);

        assertEquals(10L, result.totalOffers());
        assertEquals(6L, result.pendingOffers());
        assertEquals(3L, result.appliedOffers());
        assertEquals(1L, result.rejectedOffers());
    }

    @Test
    void shouldUseProvidedUserIdForAllMetrics() {
        Long userId = 25L;

        when(repository.countByUserId(userId)).thenReturn(4L);
        when(repository.countByUserIdAndStatus(userId, "PENDIENTE"))
                .thenReturn(2L);
        when(repository.countByUserIdAndStatus(userId, "SOLICITADA"))
                .thenReturn(1L);
        when(repository.countByUserIdAndStatus(userId, "RECHAZADA"))
                .thenReturn(1L);

        service.getDashboard(userId);

        verify(repository).countByUserId(userId);
        verify(repository).countByUserIdAndStatus(userId, "PENDIENTE");
        verify(repository).countByUserIdAndStatus(userId, "SOLICITADA");
        verify(repository).countByUserIdAndStatus(userId, "RECHAZADA");
    }
}