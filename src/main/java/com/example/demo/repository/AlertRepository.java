package com.example.demo.repository;

import com.example.demo.alert.Alert;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository
        extends JpaRepository<Alert, Long> {


    // =========================
    // GET LATEST 5 ALERTS
    // =========================

    List<Alert> findTop5ByOrderByIdDesc();


    // =========================
    // FILTER ALERTS BY SEVERITY
    // =========================

    List<Alert> findBySeverity(
            String severity
    );


    // =========================
    // CHECK EXISTING ALERT
    // =========================

    boolean existsByRobotIdAndAlertType(
            Long robotId,
            String alertType
    );

}