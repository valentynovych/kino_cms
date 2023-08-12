package com.kino_cms.dto;

import com.kino_cms.enums.FilmType;
import com.kino_cms.enums.Language;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FilmDTO {
    private Long id;
    private String name;
    private String description;
    private String mainImage;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String urlVideo;
    @Enumerated(EnumType.STRING)
    private Language language;
    @Enumerated(EnumType.STRING)
    private List<FilmType> filmTypeList;
    private String seoUrl;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;
    private Date dateOfPremiere;
    private Date dateEndPremiere;
    private String datePremiereFromTo;
    private List<Boolean> isCheckedType;

    public FilmDTO(Long id, String name, String description, String mainImage, String image1,
                   String image2, String image3, String image4, String image5, String urlVideo,
                   Language language, String seoUrl, String seoTitle, String seoKeywords, String seoDescription,
                   Date dateOfPremiere, Date dateEndPremiere, String datePremiereFromTo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.mainImage = mainImage;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.image4 = image4;
        this.image5 = image5;
        this.urlVideo = urlVideo;
        this.language = language;
        this.seoUrl = seoUrl;
        this.seoTitle = seoTitle;
        this.seoKeywords = seoKeywords;
        this.seoDescription = seoDescription;
        this.dateOfPremiere = dateOfPremiere;
        this.dateEndPremiere = dateEndPremiere;
        this.datePremiereFromTo = datePremiereFromTo;
    }

}
