package com.tochka.newsparser.service.crawler;

import com.tochka.newsparser.domain.News;
import com.tochka.newsparser.domain.NewsFeed;
import com.tochka.newsparser.repository.NewsRepository;
import com.tochka.newsparser.service.NewsFeedService;
import com.tochka.newsparser.utils.DateUtils;
import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.net.URI;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class PageCrawler implements PageProcessor {

    private static final Integer RETRY_TIMES = 3;
    private static final Integer SLEEP_TIME = 1000;

    private static final String TITLE = "title";
    private static final String CONTENT = "content";
    private static final String IMG = "img";
    private static final String DATE = "date";

    private String url;
    private String titleTag;
    private String contentTag;
    private String dateTag;
    private Site site;

    private NewsRepository repository;
    private NewsFeed newsFeed;

    public PageCrawler(NewsFeed newsFeed, NewsRepository repository) {
        this.url = newsFeed.getUrl();
        this.titleTag = newsFeed.getTitleTag();
        this.contentTag = newsFeed.getArticleTag();
        this.dateTag = newsFeed.getDatetimeTag();
        this.site = Site.me().setRetryTimes(RETRY_TIMES).setSleepTime(SLEEP_TIME);
        this.newsFeed = newsFeed;
        this.repository = repository;
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Override
    @Transactional
    public void process(Page page) {
        page.addTargetRequests(getPageLinks(page));

        page.putField(TITLE, page.getHtml().xpath("//*[@class='" + titleTag + "']").toString());
        if (page.getResultItems().get(TITLE) == null) {
            page.putField(TITLE, page.getHtml().xpath("//" + titleTag).toString());
        }
        if (page.getResultItems().get(TITLE) == null) {
            page.putField(TITLE, page.getHtml().xpath("//*[@id='" + titleTag + "']").toString());
        }
        if (page.getResultItems().get(TITLE) == null) {
            page.putField(TITLE, page.getHtml().xpath(titleTag).toString());
        }

        page.putField(CONTENT, page.getHtml().xpath("//*[@class='" + contentTag + "']").toString());
        if (page.getResultItems().get(CONTENT) == null) {
            page.putField(CONTENT, page.getHtml().xpath("//" + contentTag).toString());
        }
        if (page.getResultItems().get(CONTENT) == null) {
            page.putField(CONTENT, page.getHtml().xpath("//*[@id='" + contentTag + "']").toString());
        }
        if (page.getResultItems().get(CONTENT) == null) {
            page.putField(CONTENT, page.getHtml().xpath(contentTag).toString());
        }

        page.putField(DATE, page.getHtml().xpath("//*[@class='" + dateTag + "']").toString());
        if (page.getResultItems().get(DATE) == null) {
            page.putField(DATE, page.getHtml().xpath("//" + dateTag).toString());
        }
        if (page.getResultItems().get(DATE) == null) {
            page.putField(DATE, page.getHtml().xpath("//*[@id='" + dateTag + "']").toString());
        }
        if (page.getResultItems().get(DATE) == null) {
            page.putField(DATE, page.getHtml().xpath(dateTag).toString());
        }

        page.putField(IMG, page.getHtml().xpath("//img/@src").toString());

        News news = new News();
        page.getResultItems().getAll().forEach((k, v) -> {
            if (k.equals(TITLE) && v != null) news.setTitle(Jsoup.parse((String) v).text());
            if (k.equals(CONTENT) && v != null) news.setContent(Jsoup.parse((String) v).text());
            if (k.equals(IMG) && v != null) news.setImgUrl((String) v);
            if (k.equals(DATE) && v != null) news.setDatetime(DateUtils.getTimestampFromString(Jsoup.parse((String) v).text()));
        });

        if (news.isFullyFilled()
                && !repository.existsByNewsFeedIdAndTitleAndDatetime(newsFeed.getId(), news.getTitle(), news.getDatetime())) {
            news.setUrl(page.getUrl().get());
            news.setNewsFeed(newsFeed);
            news.setCreated(new Timestamp(System.currentTimeMillis()));
            repository.save(news);
        }

        NewsFeedService.checkIn(newsFeed);
    }

    @SneakyThrows
    private List<String> getPageLinks(Page page) {
        List<String> links = Lists.newArrayList(page.getHtml().links().all());
        String pageBody = page.getHtml().getDocument().outerHtml();

        Document document = Jsoup.parse(pageBody);
        Elements linkList = document.select("a");
        document = Jsoup.parse(pageBody, "", Parser.xmlParser());
        Elements rssLinkList = document.select("link, guid");

        linkList.forEach(it -> links.add(cleanAnchor(it.attr("abs:href"))));
        rssLinkList.forEach(it -> {
            if (it.childNodeSize() > 0) {
                links.add(it.childNode(0).outerHtml());
            }
        });

        String domain = (new URI(url)).getHost();
        return links.stream()
                .filter(item -> item.contains(domain))
                .filter(item -> !repository.existsByUrl(item))
                .distinct()
                .collect(Collectors.toList());
    }

    private String cleanAnchor(String url) {
        int index = url.indexOf('#');
        if (index != -1) {
            url = url.substring(0, index);
        }

        return url;
    }
}