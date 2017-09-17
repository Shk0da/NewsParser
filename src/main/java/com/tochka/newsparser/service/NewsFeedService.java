package com.tochka.newsparser.service;

import com.google.common.collect.Maps;
import com.tochka.newsparser.domain.NewsFeed;
import com.tochka.newsparser.repository.NewsFeedRepository;
import com.tochka.newsparser.repository.NewsRepository;
import com.tochka.newsparser.service.crawler.PageCrawler;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NewsFeedService {

    private static final Integer MIN = 60000; //millis
    private static final Logger logger = LoggerFactory.getLogger(NewsFeedService.class);
    // TODO: mb need use MQ or etc.
    private static final Map<Integer, Long> feedsQueue = Maps.newConcurrentMap();
    private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    @Autowired
    private NewsFeedRepository newsFeedRepository;

    @Autowired
    private NewsRepository newsRepository;

    public void checkNews() {
        newsFeedRepository.findAllByEnabledIsTrue().forEach(feed -> {
            Long currentTime = System.currentTimeMillis();
            Long latestCheck = feedsQueue.getOrDefault(feed.getId(), 0L);
            if ((latestCheck + (feed.getCheckFrequency() * MIN)) < currentTime) {
                collectFeed(feed);
                checkIn(feed);
            }
        });
    }

    @Async
    private void collectFeed(NewsFeed newsFeed) {
        if (newsFeed == null) return;
        executor.submit(() -> {
            logger.debug(String.format("Start collect %s", newsFeed.getUrl()));
            Spider.create(new PageCrawler(newsFeed, newsRepository)).addUrl(newsFeed.getUrl()).run();
        });
    }

    @Synchronized
    public static void checkIn(NewsFeed newsFeed) {
        feedsQueue.put(newsFeed.getId(), System.currentTimeMillis());
    }
}
