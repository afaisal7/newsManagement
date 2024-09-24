package com.appswave.newsmanagement.controller;

import com.appswave.newsmanagement.model.User;
import com.appswave.newsmanagement.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN') or #email == principal.username")
    public ResponseEntity<User> updateUser(@PathVariable String email, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(email, user));
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.softDeleteUser(email);
        return ResponseEntity.noContent().build();
    }
}

