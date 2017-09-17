package com.tochka.newsparser.controller.rest;

import com.tochka.newsparser.domain.News;
import com.tochka.newsparser.domain.NewsFeed;
import com.tochka.newsparser.repository.NewsFeedRepository;
import com.tochka.newsparser.repository.NewsRepository;
import com.tochka.newsparser.security.AuthoritiesConstants;
import com.tochka.newsparser.utils.RepositoryUtils;
import com.tochka.newsparser.utils.ResponseUtils;
import com.tochka.newsparser.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping(ApiRoutes.API_NEWSFEEDS)
@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
public class NewsFeedController {

    @Autowired
    private NewsFeedRepository newsFeedRepository;

    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/last-update")
    @Transactional(readOnly = true)
    public ResponseEntity<?> lastUpdate() throws Exception {
        Optional<News> optional = newsRepository.findFirstByNewsFeedInAndCreatedNotNullOrderByCreatedDesc(
                SecurityUtils.getCurrentUser().getNewsFeeds()
        );
        Timestamp lastUpdate = optional.map(News::getCreated).orElse(null);
        return ResponseUtils.response(lastUpdate);
    }

    @GetMapping("/total-news")
    @Transactional(readOnly = true)
    public ResponseEntity<?> totalNews() throws Exception {
        return ResponseUtils.response(newsRepository.countAllByNewsFeedIn(SecurityUtils.getCurrentUser().getNewsFeeds()));
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAll(Pageable pageable) throws Exception {
        return ResponseUtils.response(newsFeedRepository.findAllByUser(SecurityUtils.getCurrentUser(), pageable));
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody NewsFeed newsFeed) throws Exception {
        newsFeed.setUser(SecurityUtils.getCurrentUser());
        return ResponseUtils.response(RepositoryUtils.save(newsFeed, newsFeedRepository));
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody NewsFeed newsFeed) throws Exception {
        checkAndReturnFeed(newsFeed.getId());
        return ResponseUtils.response(RepositoryUtils.save(newsFeed, newsFeedRepository));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) throws Exception {
        newsFeedRepository.delete(checkAndReturnFeed(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private NewsFeed checkAndReturnFeed(Integer id) {
        NewsFeed newsFeed = newsFeedRepository.findFirstByIdAndUser(id, SecurityUtils.getCurrentUser());
        if (newsFeed == null) throw new RestExceptionHandler.NotFoundWithMessage("NewsFeed not found");

        return newsFeed;
    }
}
