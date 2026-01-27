package com.defterio.controller;

import com.defterio.dto.LoginRequest;
import com.defterio.dto.LoginResponse;
import com.defterio.dto.RegisterRequest;
import com.defterio.dto.RegisterResponse;
import com.defterio.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get access token")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        Map<String, Object> result = new HashMap<>();
        result.put("data", response);
        return ResponseEntity.ok(result);
    }
}
