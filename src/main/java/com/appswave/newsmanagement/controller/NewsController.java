package com.appswave.newsmanagement.controller;

import com.appswave.newsmanagement.model.News;
import com.appswave.newsmanagement.model.User;
import com.appswave.newsmanagement.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;
    @PostMapping("/add")
    public ResponseEntity<News> addNews(@RequestBody News news, Authentication auth) {
        User user = (User) auth.getPrincipal();
//        if (!user.isContentWriter()) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
//        }
        News createdNews = newsService.addNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    // Delete news
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id, Authentication auth) {
        User user = (User) auth.getPrincipal();
        newsService.deleteNews(id, user);
        return ResponseEntity.noContent().build();
    }

    // Get approved news
    @GetMapping("/all")
    public ResponseEntity<List<News>> getAllApprovedNews() {
        List<News> newsList = newsService.getAllApprovedNews();
        return ResponseEntity.ok(newsList);
    }
}

