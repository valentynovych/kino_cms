package com.kino_cms.dto;

import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getImage5() {
        return image5;
    }

    public void setImage5(String image5) {
        this.image5 = image5;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSeoUrl() {
        return seoUrl;
    }

    public void setSeoUrl(String seoUrl) {
        this.seoUrl = seoUrl;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }
}
