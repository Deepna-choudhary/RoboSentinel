package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    @Autowired
    private UserRepository userRepository;


    // =========================
    // REGISTER USER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

        Optional<User> existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent()) {

            return ResponseEntity
                    .badRequest()
                    .body("Username already exists!");

        }

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }


    // =========================
    // LOGIN USER
    // =========================

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {

        Optional<User> existingUser =
                userRepository.findByUsername(user.getUsername());

        if (existingUser.isEmpty()) {

            return ResponseEntity
                    .badRequest()
                    .body("User not found!");

        }

        User foundUser = existingUser.get();

        if (!foundUser.getPassword().equals(user.getPassword())) {

            return ResponseEntity
                    .badRequest()
                    .body("Incorrect password!");

        }

        return ResponseEntity.ok("Login successful!");
    }

}