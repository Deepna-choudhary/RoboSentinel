package com.example.demo.repository;

import com.example.demo.entity.Robot;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RobotRepository
        extends JpaRepository<Robot, Long> {

    List<Robot> findByNameContainingIgnoreCase(
            String name
    );

    Robot findByRobotCode(
            String robotCode
    );

    List<Robot> findByStatus(
            String status
    );

    List<Robot> findByHealthStatus(
            String healthStatus
    );

    boolean existsByRobotCode(
            String robotCode
    );
}