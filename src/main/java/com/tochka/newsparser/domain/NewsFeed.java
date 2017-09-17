package com.tochka.newsparser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "news_feeds", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "url", "user_id"}))
public class NewsFeed {

    public enum Type{RSS, HTML}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 1)
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "type")
    private Type type;

    @NotNull
    @Size(min = 1)
    @Column(name = "url")
    private String url;

    @NotNull
    @Column(name = "check_frequency")
    private Integer checkFrequency;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "title_tag")
    private String titleTag;

    @Column(name = "article_tag")
    private String articleTag;

    @Column(name = "datetime_tag")
    private String datetimeTag;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "newsFeed")
    private Set<News> news = Sets.newHashSet();

    public Boolean isRSSFeed() {
        return (type.equals(Type.RSS));
    }

    public Boolean isHTMLFeed() {
        return (type.equals(Type.HTML));
    }
}
