package com.appswave.newsmanagement.controller;

import com.appswave.newsmanagement.service.UserService;
import com.appswave.newsmanagement.util.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // Promote user to Content Writer or Admin
    @PutMapping("/promote/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> promoteUser(@PathVariable Long userId, @RequestParam Role role) {
        userService.updateUserRole(userId, role);
        return ResponseEntity.ok("User promoted to " + role);
    }
}

