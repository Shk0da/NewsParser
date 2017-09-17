package com.tochka.newsparser.repository;

import com.tochka.newsparser.domain.News;
import com.tochka.newsparser.domain.NewsFeed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Set;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    Optional<News> findFirstByNewsFeedInAndCreatedNotNullOrderByCreatedDesc(Set<NewsFeed> feeds);

    Optional<News> findFirstByNewsFeedIdIsOrderByCreatedDesc(Integer newsFeed);

    Long countAllByNewsFeedIn(Set<NewsFeed> feeds);

    Long countAllByNewsFeedId(Integer newsFeed);

    Page<News> findAllByNewsFeedId(Integer newsFeed, Pageable pageable);

    Page<News> findAllByNewsFeedIdAndTitleIgnoreCaseContaining(Integer newsFeed, String title, Pageable pageable);

    News findFirstByNewsFeedIdAndId(Integer newsFeed, Long id);

    Boolean existsByUrl(String url);
    Boolean existsByNewsFeedIdAndTitleAndDatetime(Integer newsFeed, String title, Timestamp datetime);
}
