package com.example.demo.controller;

import com.example.demo.entity.RobotTask;
import com.example.demo.service.RobotTaskService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class RobotTaskController {

    private final RobotTaskService robotTaskService;


    public RobotTaskController(
            RobotTaskService robotTaskService) {

        this.robotTaskService =
                robotTaskService;
    }


    // CREATE TASK

    @PostMapping

    public RobotTask addTask(
            @RequestBody RobotTask robotTask) {

        return robotTaskService
                .saveTask(robotTask);
    }


    // GET ALL TASKS

    @GetMapping

    public List<RobotTask> getAllTasks() {

        return robotTaskService
                .getAllTasks();
    }


    // ASSIGN TASK

    @PostMapping("/assign")

    public RobotTask assignTask(
            @RequestBody RobotTask task) {

        return robotTaskService
                .assignTask(task);
    }


    // UPDATE TASK STATUS

    @PutMapping("/{id}/status")

    public RobotTask updateTaskStatus(

            @PathVariable Long id,

            @RequestParam String status) {

        return robotTaskService
                .updateTaskStatus(
                        id,
                        status
                );
    }


    // COMPLETE TASK

    @PutMapping("/{id}/complete")

    public RobotTask completeTask(
            @PathVariable Long id) {

        return robotTaskService
                .completeTask(id);
    }


    // GET TASKS BY STATUS

    @GetMapping("/status")

    public List<RobotTask> getTasksByStatus(

            @RequestParam String status) {

        return robotTaskService
                .getTasksByStatus(status);
    }


    // DELETE TASK

    @DeleteMapping("/{id}")

    public String deleteTask(
            @PathVariable Long id) {

        robotTaskService
                .deleteTask(id);

        return "Task deleted successfully";
    }
}