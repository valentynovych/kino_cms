package com.kino_cms.dto;

import com.kino_cms.entity.FilmSession;
import com.kino_cms.entity.Hall;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class CinemaDTO {
    private Long id;
    private String name;
    private String description;
    private String conditions;
    private String logoImage;
    private String firstBanner;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    @Enumerated(EnumType.STRING)
    private Language language;
    private String seoUrl;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;
    private List<HallDTO> hallDTOList;

    public CinemaDTO(Long id, String name, String description, String conditions, String logoImage,
                     String firstBanner, String image1, String image2, String image3, String image4,
                     String image5, Language language, String seoUrl, String seoTitle, String seoKeywords,
                     String seoDescription) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.conditions = conditions;
        this.logoImage = logoImage;
        this.firstBanner = firstBanner;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.language = language;
        this.seoUrl = seoUrl;
        this.seoTitle = seoTitle;
        this.seoKeywords = seoKeywords;
        this.seoDescription = seoDescription;
    }
}
