package com.appswave.newsmanagement.model;


import com.appswave.newsmanagement.util.Role;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    @Column(unique = true)
    private String email;

    private String password;

    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;

    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private boolean isActive = true;
}
