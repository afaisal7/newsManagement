package com.appswave.newsmanagement.controller;

import com.appswave.newsmanagement.service.UserService;
import com.appswave.newsmanagement.util.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @PutMapping("/promote/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> promoteUser(@PathVariable String email, @RequestParam Role role) {
        userService.updateUserRole(email, role);
        return ResponseEntity.ok("User promoted to " + role);
    }
}

