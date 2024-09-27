package com.appswave.newsmanagement.repository;

import com.appswave.newsmanagement.model.News;
import com.appswave.newsmanagement.util.NewsState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByStateAndIsDeletedFalse(String state);
    List<News> findByPublishDateBeforeAndIsDeletedFalse(LocalDate publishDate);
}
