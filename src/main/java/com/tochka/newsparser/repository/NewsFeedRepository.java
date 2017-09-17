package com.tochka.newsparser.repository;

import com.tochka.newsparser.domain.NewsFeed;
import com.tochka.newsparser.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface NewsFeedRepository extends JpaRepository<NewsFeed, Integer> {

    Page<NewsFeed> findAllByUser(User user, Pageable pageable);

    NewsFeed findFirstByIdAndUser(Integer id, User user);

    Set<NewsFeed> findAllByEnabledIsTrue();

}
