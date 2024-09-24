package com.appswave.newsmanagement.repository;

import com.appswave.newsmanagement.model.News;
import com.appswave.newsmanagement.util.NewsState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByState(NewsState state);
}
