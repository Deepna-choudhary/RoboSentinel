package com.example.demo.controller;

import com.example.demo.alert.Alert;
import com.example.demo.service.AlertService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/alerts")
@CrossOrigin(origins = "http://localhost:5173")
public class AlertController {

    private final AlertService alertService;


    public AlertController(
            AlertService alertService) {

        this.alertService = alertService;
    }


    // ADD ALERT

    @PostMapping
    public Alert addAlert(
            @RequestBody Alert alert) {

        return alertService.saveAlert(alert);
    }


    // GET ALL ALERTS

    @GetMapping
    public List<Alert> getAllAlerts() {

        return alertService.getAllAlerts();
    }


    // GET LATEST ALERTS

    @GetMapping("/latest")
    public List<Alert> getRecentAlerts() {

        return alertService
                .getRecentAlerts();
    }


    // GET ALERT BY ID

    @GetMapping("/{id}")
    public Alert getAlertById(
            @PathVariable Long id) {

        return alertService
                .getAlertById(id);
    }


    // GET ALERTS BY SEVERITY

    @GetMapping("/severity")
    public List<Alert> getAlertsBySeverity(
            @RequestParam String severity) {

        return alertService
                .getAlertsBySeverity(
                        severity
                );
    }


    // DELETE ALERT

    @DeleteMapping("/{id}")
    public String deleteAlert(
            @PathVariable Long id) {

        alertService.deleteAlert(id);

        return "Alert deleted successfully";
    }
}