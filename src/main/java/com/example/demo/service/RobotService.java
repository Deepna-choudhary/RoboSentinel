package com.example.demo.service;

import com.example.demo.alert.Alert;
import com.example.demo.entity.Robot;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.AlertRepository;
import com.example.demo.repository.RobotRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RobotService {

    private final RobotRepository robotRepository;

    private final AlertRepository alertRepository;


    public RobotService(
            RobotRepository robotRepository,
            AlertRepository alertRepository) {

        this.robotRepository =
                robotRepository;

        this.alertRepository =
                alertRepository;
    }


    // ADD ROBOT

    public Robot saveRobot(
            Robot robot) {


        if (robotRepository.existsByRobotCode(
                robot.getRobotCode())) {

            throw new DuplicateResourceException(

                    "Robot already exists with code: "

                            + robot.getRobotCode()
            );
        }


        robot.setHealthStatus(
                checkHealth(robot)
        );


        Robot savedRobot =
                robotRepository.save(robot);


        generateAlert(savedRobot);


        return savedRobot;
    }


    // GET ALL ROBOTS

    public List<Robot> getAllRobots() {

        return robotRepository.findAll();
    }


    // SEARCH BY NAME

    public List<Robot> searchRobots(
            String name) {

        return robotRepository
                .findByNameContainingIgnoreCase(
                        name
                );
    }


    // SEARCH BY ROBOT CODE

    public Robot searchRobotByCode(
            String robotCode) {

        Robot robot =
                robotRepository
                        .findByRobotCode(
                                robotCode
                        );


        if (robot == null) {

            throw new ResourceNotFoundException(

                    "Robot not found with code: "

                            + robotCode
            );
        }


        return robot;
    }


    // GET ROBOT BY ID

    public Robot getRobotById(
            Long id) {

        return robotRepository
                .findById(id)
                .orElseThrow(() ->

                        new ResourceNotFoundException(

                                "Robot not found with ID: "

                                        + id
                        )
                );
    }


    // UPDATE ROBOT

    public Robot updateRobot(
            Long id,
            Robot robot) {


        Robot existingRobot =
                robotRepository
                        .findById(id)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(

                                        "Robot not found with ID: "

                                                + id
                                )
                        );


        Robot robotWithSameCode =
                robotRepository
                        .findByRobotCode(
                                robot.getRobotCode()
                        );


        if (robotWithSameCode != null

                &&

                !robotWithSameCode
                        .getId()
                        .equals(id)) {


            throw new DuplicateResourceException(

                    "Robot already exists with code: "

                            + robot.getRobotCode()
            );
        }


        existingRobot.setRobotCode(
                robot.getRobotCode()
        );

        existingRobot.setName(
                robot.getName()
        );

        existingRobot.setModel(
                robot.getModel()
        );

        existingRobot.setStatus(
                robot.getStatus()
        );

        existingRobot.setBatteryLevel(
                robot.getBatteryLevel()
        );

        existingRobot.setLocation(
                robot.getLocation()
        );


        existingRobot.setHealthStatus(
                checkHealth(existingRobot)
        );


        Robot updatedRobot =
                robotRepository.save(
                        existingRobot
                );


        generateAlert(updatedRobot);


        return updatedRobot;
    }


    // DELETE ROBOT

    public void deleteRobot(
            Long id) {


        Robot robot =
                robotRepository
                        .findById(id)
                        .orElseThrow(() ->

                                new ResourceNotFoundException(

                                        "Robot not found with ID: "

                                                + id
                                )
                        );


        robotRepository.delete(robot);
    }


    // CHECK HEALTH

    public String checkHealth(
            Robot robot) {


        Double batteryLevel =
                robot.getBatteryLevel();


        if (batteryLevel == null) {

            return "UNKNOWN";
        }


        if (batteryLevel < 20) {

            return "CRITICAL";
        }


        if (batteryLevel <= 50) {

            return "WARNING";
        }


        return "HEALTHY";
    }


    // GENERATE ALERT

    public void generateAlert(
            Robot robot) {


        if ("CRITICAL".equals(
                robot.getHealthStatus())) {


            Alert alert =
                    new Alert();


            alert.setRobotId(
                    robot.getId()
            );


            alert.setAlertType(
                    "LOW_BATTERY"
            );


            alert.setMessage(

                    "Robot "

                            + robot.getName()

                            + " has critically low battery: "

                            + robot.getBatteryLevel()

                            + "%"
            );


            alert.setSeverity(
                    "CRITICAL"
            );


            alertRepository.save(
                    alert
            );
        }
    }


    // GET BY STATUS

    public List<Robot> getRobotsByStatus(
            String status) {

        return robotRepository
                .findByStatus(status);
    }


    // GET BY HEALTH STATUS

    public List<Robot> getRobotsByHealthStatus(
            String healthStatus) {

        return robotRepository
                .findByHealthStatus(
                        healthStatus
                );
    }
}