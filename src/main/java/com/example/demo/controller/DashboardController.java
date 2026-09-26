package com.example.demo.controller;

import com.example.demo.service.DashboardService;
import com.example.demo.service.DashboardStats;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public DashboardStats getDashboardStats() {
        return dashboardService.getStats();
    }
}