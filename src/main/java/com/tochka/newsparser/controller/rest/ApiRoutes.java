package com.tochka.newsparser.controller.rest;

public interface ApiRoutes {
    String API_PATH = "/api";
    String API_NEWSFEEDS = API_PATH + "/news-feeds";
    String API_NEWS = API_NEWSFEEDS + "/{newsFeedId}/news";
}
