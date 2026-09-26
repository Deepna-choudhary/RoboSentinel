package com.example.demo.service;

public class DashboardStats {

    private long totalRobots;
    private long healthyRobots;
    private long criticalRobots;
    private long warningRobots;
    private long totalTasks;
    private long completedTasks;
    private long pendingTasks;
    private long inProgressTasks;
    private long totalAlerts;
    private long criticalAlerts;
    private long highAlerts;

    public DashboardStats(
            long totalRobots,
            long healthyRobots,
            long criticalRobots,
            long warningRobots,
            long totalTasks,
            long completedTasks,
            long pendingTasks,
            long inProgressTasks,
            long totalAlerts,
            long criticalAlerts,
            long highAlerts) {

        this.totalRobots = totalRobots;
        this.healthyRobots = healthyRobots;
        this.criticalRobots = criticalRobots;
        this.warningRobots = warningRobots;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.pendingTasks = pendingTasks;
        this.inProgressTasks = inProgressTasks;
        this.totalAlerts = totalAlerts;
        this.criticalAlerts = criticalAlerts;
        this.highAlerts = highAlerts;
    }

    public long getTotalRobots() {
        return totalRobots;
    }

    public long getHealthyRobots() {
        return healthyRobots;
    }

    public long getCriticalRobots() {
        return criticalRobots;
    }

    public long getWarningRobots() {
        return warningRobots;
    }

    public long getTotalTasks() {
        return totalTasks;
    }

    public long getCompletedTasks() {
        return completedTasks;
    }

    public long getPendingTasks() {
        return pendingTasks;
    }

    public long getInProgressTasks() {
        return inProgressTasks;
    }

    public long getTotalAlerts() {
        return totalAlerts;
    }

    public long getCriticalAlerts() {
        return criticalAlerts;
    }

    public long getHighAlerts() {
        return highAlerts;
    }
}