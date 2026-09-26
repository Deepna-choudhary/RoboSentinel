package com.example.demo.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import com.example.demo.entity.Robot;
import com.example.demo.service.RobotService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://localhost:5174"
})
public class RobotController {

    private final RobotService robotService;

    public RobotController(RobotService robotService) {
        this.robotService = robotService;
    }

    // ADD ROBOT
    @PostMapping("/robots")
    public Robot addRobot(@Valid @RequestBody Robot robot) {
        return robotService.saveRobot(robot);
    }

    // GET ALL ROBOTS
    @GetMapping("/robots")
    public List<Robot> getAllRobots() {
        return robotService.getAllRobots();
    }

    // SEARCH ROBOTS BY NAME
    @GetMapping("/robots/search")
    public List<Robot> searchRobots(
            @RequestParam String name) {

        return robotService.searchRobots(name);
    }

    // UPDATE ROBOT
    @PutMapping("/robots/{id}")
    public Robot updateRobot(
            @PathVariable Long id,
            @Valid @RequestBody Robot robot) {

        return robotService.updateRobot(id, robot);
    }

    // DELETE ROBOT
    @DeleteMapping("/robots/{id}")
    public String deleteRobot(@PathVariable Long id) {

        robotService.deleteRobot(id);

        return "Robot deleted successfully";
    }

    // GET ROBOT BY ID
    @GetMapping("/robots/{id}")
    public Robot getRobotById(@PathVariable Long id) {

        return robotService.getRobotById(id);
    }

    // SEARCH ROBOT BY CODE
    @GetMapping("/robots/search/code")
    public Robot searchRobotByCode(
            @RequestParam String robotCode) {

        return robotService.searchRobotByCode(robotCode);
    }

    // GET ROBOTS BY STATUS
    @GetMapping("/robots/status")
    public List<Robot> getRobotsByStatus(
            @RequestParam String status) {

        return robotService.getRobotsByStatus(status);
    }

    // GET ROBOTS BY HEALTH STATUS
    @GetMapping("/robots/health")
    public List<Robot> getRobotsByHealthStatus(
            @RequestParam String healthStatus) {

        return robotService.getRobotsByHealthStatus(healthStatus);
    }
}