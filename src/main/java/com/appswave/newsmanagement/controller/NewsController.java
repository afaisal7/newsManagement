package com.appswave.newsmanagement.controller;

import com.appswave.newsmanagement.dto.UserDto;
import com.appswave.newsmanagement.mapper.UserMapper;
import com.appswave.newsmanagement.model.News;
import com.appswave.newsmanagement.service.NewsService;
import com.appswave.newsmanagement.service.UserService;
import com.appswave.newsmanagement.util.Role;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/news")
public class NewsController {
    private final NewsService newsService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/add")
    public ResponseEntity<News> addNews(@RequestBody News news, Authentication auth) {
        UserDto userDto = userMapper.toDto(userService.getUserByEmail(auth.getName()));
        userService.checkUserRole(userDto, Arrays.asList(Role.CONTENT_WRITER.name(), Role.ADMIN.name()));
        News createdNews = newsService.addNews(news);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNews);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id, Authentication auth) {
        UserDto userDto = userMapper.toDto(userService.getUserByEmail(auth.getName()));
        newsService.deleteNews(id, userDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<News>> getAllApprovedNews() {
        List<News> newsList = newsService.getAllApprovedNews();
        return ResponseEntity.ok(newsList);
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveNews(@PathVariable Long id, Authentication auth) {
        UserDto userDto = userMapper.toDto(userService.getUserByEmail(auth.getName()));
        userService.checkUserRole(userDto, List.of(Role.ADMIN.name()));
        newsService.approveNews(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> rejectNews(@PathVariable Long id, Authentication auth) {
        UserDto userDto = userMapper.toDto(userService.getUserByEmail(auth.getName()));
        userService.checkUserRole(userDto, List.of(Role.ADMIN.name()));
        newsService.rejectNews(id);
        return ResponseEntity.noContent().build();
    }
}