package com.kino_cms.dto;

import com.kino_cms.enums.Language;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class HallDTO {
    private Long id;
    private String name;
    private String descriptions;
    private String hallSchema; //image
    private String firstBanner;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    @Enumerated(EnumType.STRING)
    private Language language;
    private String createTime;
    private Long cinemaId;
    private String seoUrl;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;

    public HallDTO(Long id, String name, String descriptions, String hallSchema, String firstBanner,
                   String image1, String image2, String image3, String image4, String image5,
                   Language language, String createTime, Long cinemaId,
                   String seoUrl, String seoTitle, String seoKeywords, String seoDescription) {
        this.id = id;
        this.name = name;
        this.descriptions = descriptions;
        this.hallSchema = hallSchema;
        this.firstBanner = firstBanner;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.language = language;
        this.createTime = createTime;
        this.cinemaId = cinemaId;
        this.seoUrl = seoUrl;
        this.seoTitle = seoTitle;
        this.seoKeywords = seoKeywords;
        this.seoDescription = seoDescription;
    }
}
