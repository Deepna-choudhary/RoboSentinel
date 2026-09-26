package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // =========================
    // ADD USER
    // =========================
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {

        return ResponseEntity.ok(
                userService.addUser(user)
        );
    }


    // =========================
    // GET ALL USERS
    // =========================
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {

        return ResponseEntity.ok(
                userService.getAllUsers()
        );
    }


    // =========================
    // GET USER BY USERNAME
    // =========================
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(
            @PathVariable String username) {

        User user =
                userService.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }
}