package com.appswave.newsmanagement.serviceImpl;

import com.appswave.newsmanagement.dto.UserDto;
import com.appswave.newsmanagement.model.News;
import com.appswave.newsmanagement.repository.NewsRepository;
import com.appswave.newsmanagement.service.NewsService;
import com.appswave.newsmanagement.util.NewsEvents;
import com.appswave.newsmanagement.util.NewsState;
import com.appswave.newsmanagement.util.Role;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class NewsServiceImpl implements NewsService {
    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;
    private final StateMachine<NewsState, NewsEvents> stateMachine;

    @Override
    public News addNews(News news) {
        news.setState(NewsState.PENDING.name());
        return newsRepository.save(news);
    }

    @Override
    public void deleteNews(Long newsId, UserDto userDto) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));
        if (canDeleteNews(news, userDto)) {
            newsRepository.delete(news);
        } else {
            throw new IllegalStateException("Unauthorized to delete news");
        }
    }

    @Override
    public List<News> getAllApprovedNews() {
        return newsRepository.findByStateAndIsDeletedFalse(NewsState.APPROVED.name());
    }

    @Override
    public void approveNews(Long id) {
        News news = getNewsById(id);
        if (news.getState().equals(NewsState.PENDING.name())) {
            transitionState(news, NewsEvents.APPROVE);
        } else {
            throw new IllegalStateException("Only pending news can be approved.");
        }
    }

    @Override
    public void rejectNews(Long id) {
        News news = getNewsById(id);
        if (news.getState().equals(NewsState.PENDING.name())) {
            transitionState(news, NewsEvents.REJECT);
        } else {
            throw new IllegalStateException("Only pending news can be rejected.");
        }
    }

    private News getNewsById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("News not found"));
    }

    private void transitionState(News news, NewsEvents event) {
        stateMachine.start();
        stateMachine.sendEvent(MessageBuilder.withPayload(event).build());
        news.setState(stateMachine.getState().getId().name());
        newsRepository.save(news);
    }
    private boolean canDeleteNews(News news, UserDto userDto) {
        boolean isPending = news.getState().equals(NewsState.PENDING.name());
        boolean isContentWriter = userDto.getRole().equalsIgnoreCase(Role.CONTENT_WRITER.name());
        boolean isAdmin = userDto.getRole().equalsIgnoreCase(Role.ADMIN.name());

        return (isPending && (isContentWriter || isAdmin)) || isAdmin;
    }
}