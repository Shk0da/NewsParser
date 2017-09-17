package com.tochka.newsparser;

import com.tochka.newsparser.service.NewsFeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
@EnableScheduling
public class Scheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NewsFeedService newsFeedService;

    @Scheduled(cron = "0 * * * * *")
    public void checkNews() {
        logger.debug("Start checkNews");
        newsFeedService.checkNews();
    }
}
