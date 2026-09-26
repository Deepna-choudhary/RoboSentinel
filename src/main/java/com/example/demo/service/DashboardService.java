package com.example.demo.service;

import com.example.demo.alert.Alert;
import com.example.demo.entity.Robot;
import com.example.demo.entity.RobotTask;
import com.example.demo.repository.AlertRepository;
import com.example.demo.repository.RobotRepository;
import com.example.demo.repository.RobotTaskRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final RobotRepository robotRepository;
    private final RobotTaskRepository robotTaskRepository;
    private final AlertRepository alertRepository;


    public DashboardService(
            RobotRepository robotRepository,
            RobotTaskRepository robotTaskRepository,
            AlertRepository alertRepository) {

        this.robotRepository = robotRepository;
        this.robotTaskRepository = robotTaskRepository;
        this.alertRepository = alertRepository;
    }


    public DashboardStats getStats() {

        // =========================
        // ROBOTS
        // =========================

        List<Robot> robots = robotRepository.findAll();

        long totalRobots = robots.size();

        long healthyRobots = robots.stream()
                .filter(robot ->
                        "HEALTHY".equals(robot.getHealthStatus()))
                .count();

        long criticalRobots = robots.stream()
                .filter(robot ->
                        "CRITICAL".equals(robot.getHealthStatus()))
                .count();

        long warningRobots = robots.stream()
                .filter(robot ->
                        "WARNING".equals(robot.getHealthStatus()))
                .count();


        // =========================
        // TASKS
        // =========================

        List<RobotTask> tasks = robotTaskRepository.findAll();

        long totalTasks = tasks.size();

        long completedTasks = tasks.stream()
                .filter(task ->
                        "COMPLETED".equals(task.getTaskStatus()))
                .count();

        long pendingTasks = tasks.stream()
                .filter(task ->
                        "PENDING".equals(task.getTaskStatus()))
                .count();

        long inProgressTasks = tasks.stream()
                .filter(task ->
                        "IN_PROGRESS".equals(task.getTaskStatus()))
                .count();


        // =========================
        // ALERTS
        // =========================

        List<Alert> alerts = alertRepository.findAll();

        long totalAlerts = alerts.size();

        long criticalAlerts = alerts.stream()
                .filter(alert ->
                        "CRITICAL".equals(alert.getSeverity()))
                .count();

        long highAlerts = alerts.stream()
                .filter(alert ->
                        "HIGH".equals(alert.getSeverity()))
                .count();


        return new DashboardStats(
                totalRobots,
                healthyRobots,
                criticalRobots,
                warningRobots,
                totalTasks,
                completedTasks,
                pendingTasks,
                inProgressTasks,
                totalAlerts,
                criticalAlerts,
                highAlerts
        );
    }
}