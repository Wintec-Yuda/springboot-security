package com.security.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.security.demo.users.UserService;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    // Endpoint untuk Register
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        if (userService.findByUsername(registerRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>(new AuthResponse("Username is already taken!", null), HttpStatus.BAD_REQUEST);
        }

        userService.registerUser(registerRequest.getUsername(), registerRequest.getPassword(),
                registerRequest.getRole());
        return new ResponseEntity<>(new AuthResponse("User registered successfully!", null), HttpStatus.CREATED);
    }

    // Endpoint untuk Login
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        if (token != null) {
            return ResponseEntity.ok(new AuthResponse("Login successful!", token));
        } else {
            return new ResponseEntity<>(new AuthResponse("Invalid credentials!", null), HttpStatus.UNAUTHORIZED);
        }
    }
}
