package com.example.demo.alert;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Alert {

    // =========================
    // ID
    // =========================

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;


    // =========================
    // ROBOT ID
    // =========================

    @NotNull(
            message = "Robot ID cannot be empty"
    )
    private Long robotId;


    // =========================
    // ALERT TYPE
    // =========================

    @NotBlank(
            message = "Alert type cannot be empty"
    )
    private String alertType;


    // =========================
    // ALERT MESSAGE
    // =========================

    @NotBlank(
            message = "Alert message cannot be empty"
    )
    private String message;


    // =========================
    // SEVERITY
    // =========================

    @NotBlank(
            message = "Severity cannot be empty"
    )
    private String severity;


    // =========================
    // GET ID
    // =========================

    public Long getId() {

        return id;
    }


    // =========================
    // SET ID
    // =========================

    public void setId(Long id) {

        this.id = id;
    }


    // =========================
    // GET ROBOT ID
    // =========================

    public Long getRobotId() {

        return robotId;
    }


    // =========================
    // SET ROBOT ID
    // =========================

    public void setRobotId(Long robotId) {

        this.robotId = robotId;
    }


    // =========================
    // GET ALERT TYPE
    // =========================

    public String getAlertType() {

        return alertType;
    }


    // =========================
    // SET ALERT TYPE
    // =========================

    public void setAlertType(
            String alertType) {

        this.alertType = alertType;
    }


    // =========================
    // GET MESSAGE
    // =========================

    public String getMessage() {

        return message;
    }


    // =========================
    // SET MESSAGE
    // =========================

    public void setMessage(
            String message) {

        this.message = message;
    }


    // =========================
    // GET SEVERITY
    // =========================

    public String getSeverity() {

        return severity;
    }


    // =========================
    // SET SEVERITY
    // =========================

    public void setSeverity(
            String severity) {

        this.severity = severity;
    }
}