package com.appswave.newsmanagement.service;

import com.appswave.newsmanagement.dto.UserDto;
import com.appswave.newsmanagement.mapper.UserMapper;
import com.appswave.newsmanagement.dto.UserLoginDto;
import com.appswave.newsmanagement.dto.UserRegistrationDto;
import com.appswave.newsmanagement.exception.EmailAlreadyTakenException;
import com.appswave.newsmanagement.exception.InvalidPasswordException;
import com.appswave.newsmanagement.exception.UserNotFoundException;
import com.appswave.newsmanagement.model.User;
import com.appswave.newsmanagement.repository.UserRepository;
import com.appswave.newsmanagement.util.JwtUtil;
import com.appswave.newsmanagement.util.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

import java.util.Collection;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDto registrationDTO) {
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyTakenException("Email already taken: " + registrationDTO.getEmail());
        }
        User newUser = new User();
        newUser.setFullName(registrationDTO.getFullName());
        newUser.setEmail(registrationDTO.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        newUser.setDateOfBirth(registrationDTO.getDateOfBirth());
        newUser.setRole(Role.NORMAL);
        userRepository.save(newUser);
    }

    public User authenticateUser(UserLoginDto loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new InvalidPasswordException("Invalid email or password"));
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid email or password");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findByActiveTrue();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    public User updateUser(String email, UserRegistrationDto userDetails) {
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

    public void updateUserRole(String email, Role role) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user)
        );
    }
    public void checkUserRole(UserDto userDto, List<String> roles) {
        if (!roles.contains(userDto.getRole())) {
            throw new AccessDeniedException("User does not have the required role.");
        }
    }
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }
}
