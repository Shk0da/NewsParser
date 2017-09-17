package com.tochka.newsparser.controller.rest;

import com.tochka.newsparser.domain.News;
import com.tochka.newsparser.repository.NewsRepository;
import com.tochka.newsparser.security.AuthoritiesConstants;
import com.tochka.newsparser.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

@RestController
@RequestMapping(ApiRoutes.API_NEWS)
@Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.USER})
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<?> getAll(@PathVariable Integer newsFeedId, Pageable pageable) throws Exception {
        return ResponseUtils.response(newsRepository.findAllByNewsFeedId(newsFeedId, pageable));
    }

    @GetMapping("/search")
    @Transactional(readOnly = true)
    public ResponseEntity<?> search(@PathVariable Integer newsFeedId, @RequestParam String title, Pageable pageable) throws Exception {
        return ResponseUtils.response(newsRepository.findAllByNewsFeedIdAndTitleIgnoreCaseContaining(newsFeedId, title, pageable));
    }

    @GetMapping("/total")
    @Transactional(readOnly = true)
    public ResponseEntity<?> total(@PathVariable Integer newsFeedId) throws Exception {
        return ResponseUtils.response(newsRepository.countAllByNewsFeedId(newsFeedId));
    }

    @GetMapping("/last-update")
    @Transactional(readOnly = true)
    public ResponseEntity<?> lastUpdate(@PathVariable Integer newsFeedId) throws Exception {
        Optional<News> optional = newsRepository.findFirstByNewsFeedIdIsOrderByCreatedDesc(newsFeedId);
        Timestamp lastUpdate = optional.map(News::getCreated).orElse(null);
        return ResponseUtils.response(lastUpdate);
    }

    @GetMapping("/{newsId}")
    @Transactional(readOnly = true)
    public ResponseEntity<?> article(@PathVariable Integer newsFeedId, @PathVariable Long newsId) throws Exception {
        return ResponseUtils.response(newsRepository.findFirstByNewsFeedIdAndId(newsFeedId, newsId));
    }
}
