package com.appswave.newsmanagement.model;

import com.appswave.newsmanagement.util.NewsState;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String titleArabic;
    private String description;
    private String descriptionArabic;
    private String imageUrl;
    private LocalDate publishDate;
    @Enumerated(EnumType.STRING)
    private NewsState state = NewsState.PENDING; // Default state is "Pending"

    // Getters, setters, constructors
}

