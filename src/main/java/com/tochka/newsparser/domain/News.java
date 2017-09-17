package com.tochka.newsparser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "datetime")
    private Timestamp datetime;

    @Column(name = "created")
    private Timestamp created;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "feed_id")
    private NewsFeed newsFeed;

    public Boolean isFullyFilled() {
        return (title != null && content != null && datetime != null);
    }
}
