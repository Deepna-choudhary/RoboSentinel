package com.example.demo.service;

import com.example.demo.alert.Alert;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AlertRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;


    // =========================
    // CONSTRUCTOR
    // =========================

    public AlertService(
            AlertRepository alertRepository) {

        this.alertRepository = alertRepository;
    }


    // =========================
    // GET ALL ALERTS
    // =========================

    public List<Alert> getAllAlerts() {

        return alertRepository.findAll();
    }


    // =========================
    // GET ALERT BY ID
    // =========================

    public Alert getAlertById(Long id) {

        return alertRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Alert not found with ID: " + id
                        )
                );
    }


    // =========================
    // SAVE ALERT
    // =========================

    public Alert saveAlert(Alert alert) {

        return alertRepository.save(alert);
    }


    // =========================
    // GET LATEST 5 ALERTS
    // =========================

    public List<Alert> getRecentAlerts() {

        return alertRepository
                .findTop5ByOrderByIdDesc();
    }


    // =========================
    // FILTER ALERTS BY SEVERITY
    // =========================

    public List<Alert> getAlertsBySeverity(
            String severity) {

        return alertRepository
                .findBySeverity(severity);
    }


    // =========================
    // DELETE ALERT
    // =========================

    public void deleteAlert(Long id) {

        Alert alert = alertRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Alert not found with ID: " + id
                        )
                );

        alertRepository.delete(alert);
    }
}