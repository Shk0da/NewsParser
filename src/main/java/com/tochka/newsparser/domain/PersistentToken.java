package com.tochka.newsparser.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "persistent_token")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PersistentToken implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final int MAX_USER_AGENT_LEN = 255;

    @Id
    private String series;

    @JsonIgnore
    @NotNull
    @Column(name = "token_value", nullable = false)
    private String tokenValue;

    @Column(name = "token_date")
    private LocalDate tokenDate;

    //an IPV6 address max length is 39 characters
    @Size(max = 39)
    @Column(name = "ip_address", length = 39)
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @JsonIgnore
    @ManyToOne
    private User user;
}