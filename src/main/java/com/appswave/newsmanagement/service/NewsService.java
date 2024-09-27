package com.appswave.newsmanagement.service;

import com.appswave.newsmanagement.dto.UserDto;
import com.appswave.newsmanagement.model.News;

import java.util.List;

public interface NewsService {
    News addNews(News news);
    void deleteNews(Long newsId, UserDto userDto);

    void approveNews(Long id);
    List<News> getAllApprovedNews();

    void rejectNews(Long id);
}