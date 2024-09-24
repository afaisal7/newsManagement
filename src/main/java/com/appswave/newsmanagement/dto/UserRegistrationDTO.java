package com.appswave.newsmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.Date;

@Data
public class UserRegistrationDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotBlank(message = "Date of birth is required")
    private Date dateOfBirth;

    private String role;
}
