package com.appswave.newsmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titleEn;
    private String titleAr;
    private String descEn;
    private String descAr;
    private String imageUrl;
    private LocalDate publishDate;
    private String state;
    private boolean isDeleted = false;
}

