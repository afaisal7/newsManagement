package com.appswave.newsmanagement.controller;

import com.appswave.newsmanagement.dto.TokenResponse;
import com.appswave.newsmanagement.dto.UserLoginDto;
import com.appswave.newsmanagement.dto.UserRegistrationDto;
import com.appswave.newsmanagement.exception.EmailAlreadyTakenException;
import com.appswave.newsmanagement.exception.InvalidPasswordException;
import com.appswave.newsmanagement.model.User;
import com.appswave.newsmanagement.service.UserService;
import com.appswave.newsmanagement.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto registrationDTO) {
        try {
            userService.registerUser(registrationDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (EmailAlreadyTakenException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> loginUser(@Valid @RequestBody UserLoginDto loginDTO) {
        try {
            User user = userService.authenticateUser(loginDTO);
            String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
            return ResponseEntity.ok(new TokenResponse(accessToken, refreshToken));
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        String username = jwtUtil.extractUsernameFromRefreshToken(refreshToken);

        if (Boolean.TRUE.equals(jwtUtil.validateRefreshToken(refreshToken, username))) {
            User user = userService.getUserByEmail(username);
            String newAccessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
            return ResponseEntity.ok(new TokenResponse(newAccessToken, refreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
