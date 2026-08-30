package com.example.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hr.dto.LoginRequest;
import com.example.hr.dto.LoginResponse;
import com.example.hr.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authenticationService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authenticationService.register(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
