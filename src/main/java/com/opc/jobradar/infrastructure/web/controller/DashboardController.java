package com.opc.jobradar.infrastructure.web.controller;

import com.opc.jobradar.application.service.DashboardResponse;
import com.opc.jobradar.application.service.GetDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Adaptador HTTP para consultar las métricas del dashboard.
 */
@Tag(
        name = "Dashboard",
        description = "Métricas de las ofertas del usuario autenticado"
)
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private static final String USER_ID = "USER_ID";

    private final GetDashboardService getDashboardService;

    public DashboardController(
            GetDashboardService getDashboardService
    ) {
        this.getDashboardService = getDashboardService;
    }

    /**
     * Obtiene las métricas de ofertas del usuario autenticado.
     *
     * @param session sesión HTTP del usuario autenticado
     * @return métricas del dashboard
     */
    @Operation(
            summary = "Obtener métricas del dashboard",
            description = "Obtiene las métricas de ofertas del usuario autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Métricas obtenidas correctamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Usuario no autenticado"
            )
    })
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard(
            HttpSession session
    ) {
        Long userId = (Long) session.getAttribute(USER_ID);

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        DashboardResponse response =
                getDashboardService.getDashboard(userId);

        return ResponseEntity.ok(response);
    }
}