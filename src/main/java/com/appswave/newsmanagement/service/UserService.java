package com.appswave.newsmanagement.service;

import com.appswave.newsmanagement.config.mapper.UserMapper;
import com.appswave.newsmanagement.dto.UserLoginDTO;
import com.appswave.newsmanagement.dto.UserRegistrationDTO;
import com.appswave.newsmanagement.model.User;
import com.appswave.newsmanagement.repository.UserRepository;
import com.appswave.newsmanagement.util.JwtUtil;
import com.appswave.newsmanagement.util.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        String encodedPassword = passwordEncoder.encode(registrationDTO.getPassword());

        User newUser = new User();
        newUser.setFullName(registrationDTO.getFullName());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(encodedPassword);
        newUser.setDateOfBirth(registrationDTO.getDateOfBirth());
        newUser.setRole(Role.NORMAL);

        return userRepository.save(newUser);
    }

    public String authenticateUser(UserLoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return generateToken(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findByIsActiveTrue();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public User updateUser(String email, User userDetails) {
        User user = getUserByEmail(email);
        user.setFullName(userDetails.getFullName());
        user.setDateOfBirth(userDetails.getDateOfBirth());
        return userRepository.save(user);
    }

    public void softDeleteUser(String email) {
        User user = getUserByEmail(email);
        user.setActive(false);
        userRepository.save(user);
    }
    public void updateUserRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }


    private String generateToken(User user) {
        return jwtUtil.generateToken(user.getEmail());
    }
}
