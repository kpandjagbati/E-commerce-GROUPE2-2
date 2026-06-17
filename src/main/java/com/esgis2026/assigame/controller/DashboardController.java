package com.esgis2026.assigame.controller;

import com.esgis2026.assigame.dto.DashboardStatsDto;
import com.esgis2026.assigame.dto.VendeurDashboardStatsDto;
import com.esgis2026.assigame.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardStatsDto getAdminDashboard() {
        return dashboardService.getAdminStats();
    }

    @GetMapping("/vendeur")
    @PreAuthorize("hasRole('VENDEUR')")
    public VendeurDashboardStatsDto getVendeurDashboard() {
        return dashboardService.getVendeurStats();
    }
}
