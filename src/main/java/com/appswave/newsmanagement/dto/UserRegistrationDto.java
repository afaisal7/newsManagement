package com.appswave.newsmanagement.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


import java.util.Date;

@Data
public class UserRegistrationDto {
    @NotBlank(message = "Full name is required")
    private String fullName;
    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private Date dateOfBirth;
    private String role;
}
