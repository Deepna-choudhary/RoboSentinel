package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.entity.Robot;
import com.example.demo.entity.RobotTask;
import com.example.demo.repository.RobotRepository;
import com.example.demo.repository.RobotTaskRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class RobotTaskService {

    private final RobotTaskRepository robotTaskRepository;
    private final RobotRepository robotRepository;

    public RobotTaskService(
            RobotTaskRepository robotTaskRepository,
            RobotRepository robotRepository) {

        this.robotTaskRepository = robotTaskRepository;
        this.robotRepository = robotRepository;
    }


    // CREATE TASK

    public RobotTask saveTask(RobotTask robotTask) {

        return robotTaskRepository.save(robotTask);
    }


    // GET ALL TASKS

    public List<RobotTask> getAllTasks() {

        return robotTaskRepository.findAll();
    }


    // GET TASKS BY STATUS

    public List<RobotTask> getTasksByStatus(String status) {

        return robotTaskRepository.findByTaskStatus(status);
    }


    // ASSIGN TASK

    public RobotTask assignTask(RobotTask robotTask) {

        Robot robot = robotRepository
                .findById(robotTask.getRobotId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Robot not found with ID: "
                                        + robotTask.getRobotId()
                        )
                );


        if (!"HEALTHY".equals(robot.getHealthStatus())) {

            throw new RuntimeException(
                    "Task cannot be assigned. Robot is not healthy."
            );
        }


        // Change robot status

        robot.setStatus("BUSY");

        robotRepository.save(robot);


        // Set task status

        robotTask.setTaskStatus("PENDING");


        return robotTaskRepository.save(robotTask);
    }


    // UPDATE TASK STATUS

    public RobotTask updateTaskStatus(
            Long id,
            String status) {

        RobotTask task =
                robotTaskRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Task not found with ID: "
                                                + id
                                )
                        );


        task.setTaskStatus(status);


        return robotTaskRepository.save(task);
    }


    // COMPLETE TASK

    public RobotTask completeTask(Long id) {

        RobotTask task = robotTaskRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with ID: " + id
                        )
                );

        // Task completed
        task.setTaskStatus("COMPLETED");

        RobotTask completedTask =
                robotTaskRepository.save(task);


        // Find assigned robot
        Robot robot = robotRepository
                .findById(task.getRobotId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Robot not found with ID: "
                                        + task.getRobotId()
                        )
                );


        // Robot becomes IDLE again
        robot.setStatus("IDLE");

        robotRepository.save(robot);


        return completedTask;
    }


    // DELETE TASK

    public void deleteTask(Long id) {

        RobotTask task =
                robotTaskRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Task not found with ID: "
                                                + id
                                )
                        );


        robotTaskRepository.delete(task);
    }
}