package com.kino_cms.dto;

import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;

public class GeneralPageDTO {
    private Long id;
    @Enumerated(EnumType.STRING)
    private PageType pageType;
    private String title;
    private String description;
    private String mainImage;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Boolean isActive;
    private String createTime;
    private String seoUrl;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;
}
