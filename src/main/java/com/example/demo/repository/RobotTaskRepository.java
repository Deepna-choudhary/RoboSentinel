package com.example.demo.repository;

import com.example.demo.entity.RobotTask;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RobotTaskRepository
        extends JpaRepository<RobotTask, Long> {

    List<RobotTask> findByTaskStatus(String taskStatus);

}