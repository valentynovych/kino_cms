package com.kino_cms.entity;

import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class FeedPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "feed_type")
    private FeedType feedType;
    private String title;
    @Column(length = 2000)
    private String description;
    private Date publishDate;
    private String mainImage;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String urlVideo;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private SeoBlock seoBlock;
    private Boolean isActivate;
    @Enumerated(EnumType.STRING)
    private Language language;
    private String createTime;
    private String tags;
    private Long translatePageId;
}
