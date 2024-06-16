package org.example.lookbok_be.controller;

import org.example.lookbok_be.dto.JoinDTO;
import org.example.lookbok_be.entity.UserEntity;
import org.example.lookbok_be.service.JoinService;
import org.example.lookbok_be.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {

        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody JoinDTO joinDTO) {
        userService.registerUser(joinDTO);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JoinDTO joinDTO) {
        String token = userService.loginUser(joinDTO);
        UserEntity user = userService.findByUsername(joinDTO.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("username", user.getUsername());

        return ResponseEntity.ok(response);
    }
}