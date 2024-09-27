package com.appswave.newsmanagement.util;

import com.appswave.newsmanagement.model.News;
import com.appswave.newsmanagement.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InTimeScheduler {
    private final NewsRepository newsRepository;
    @Scheduled(cron = "${in.time.cron}")
    public void softDeleteExpiredNews() {
        log.info("Starting scheduled task: softDeleteExpiredNews");
        LocalDate currentDate = LocalDate.now();
        List<News> expiredNews = newsRepository.findByPublishDateBeforeAndIsDeletedFalse(currentDate);
        log.info("Found {} expired news items to soft delete.", expiredNews.size());
        expiredNews.forEach(news -> {
            news.setDeleted(true);
            newsRepository.save(news);
            log.debug("Soft deleted news item with ID: {}", news.getId());
        });
        log.info("Completed scheduled task: softDeleteExpiredNews");
    }
}
