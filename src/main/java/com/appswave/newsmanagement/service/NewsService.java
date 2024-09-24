package com.appswave.newsmanagement.service;

import com.appswave.newsmanagement.model.News;
import com.appswave.newsmanagement.model.User;
import com.appswave.newsmanagement.repository.NewsRepository;
import com.appswave.newsmanagement.util.NewsEvents;
import com.appswave.newsmanagement.util.NewsState;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private final StateMachine<NewsState, NewsEvents> stateMachine;

    public News addNews(News news) {
        news.setState(NewsState.PENDING); // Set default state to Pending
        return newsRepository.save(news);
    }

    public void deleteNews(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));

        if (news.getState() == NewsState.PENDING /*|| user.isAdmin()*/) {
            newsRepository.delete(news);
        } else {
            throw new IllegalStateException("Cannot delete approved news without admin approval.");
        }
    }

    public News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));
    }

    public List<News> getAllApprovedNews() {
        return newsRepository.findByState(NewsState.APPROVED);
    }
}

