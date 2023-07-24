package com.kino_cms.entity;

import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@NoArgsConstructor
@Data
@Entity
public class FeedPage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private FeedType feedType;
    private String title;
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
}