package com.kino_cms.dto;

import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
@Getter
@Setter
public class FeedDTO {
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
    private Boolean isActivate;
    @Enumerated(EnumType.STRING)
    private Language language;
    private String createTime;
    private List<String> tags;
    private Long seoBlockId;
    private String seoUrl;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;
    private String publishDateString;

    public FeedDTO() {
    }

    public FeedDTO(Long id, FeedType feedType, String title, String description, Date publishDate, String mainImage,
                   String image1, String image2, String image3, String image4, String image5, String urlVideo,
                   Boolean isActivate, Language language, String createTime, Long seoBlockId,
                   String seoUrl, String seoTitle, String seoKeywords, String seoDescription) {
        this.id = id;
        this.feedType = feedType;
        this.title = title;
        this.description = description;
        this.publishDate = publishDate;
        this.mainImage = mainImage;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.urlVideo = urlVideo;
        this.isActivate = isActivate;
        this.language = language;
        this.createTime = createTime;
        this.tags = Arrays.stream(seoKeywords.split(",")).toList();
        this.seoBlockId = seoBlockId;
        this.seoUrl = seoUrl;
        this.seoTitle = seoTitle;
        this.seoKeywords = seoKeywords;
        this.seoDescription = seoDescription;
        this.publishDateString = publishDate.toString();
    }
}
