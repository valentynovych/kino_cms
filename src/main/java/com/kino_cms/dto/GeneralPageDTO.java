package com.kino_cms.dto;

import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
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

    public GeneralPageDTO(){

    }

    public GeneralPageDTO(Long id, PageType pageType, String title, String description,
                          String mainImage, String image1, String image2, String image3,
                          String image4, String image5, Language language, Boolean isActive,
                          String createTime, String seoUrl, String seoTitle, String seoKeywords,
                          String seoDescription) {
        this.id = id;
        this.pageType = pageType;
        this.title = title;
        this.description = description;
        this.mainImage = mainImage;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.language = language;
        this.isActive = isActive;
        this.createTime = createTime;
        this.seoUrl = seoUrl;
        this.seoTitle = seoTitle;
        this.seoKeywords = seoKeywords;
        this.seoDescription = seoDescription;
    }
}
