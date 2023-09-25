package com.kino_cms.entity;

import com.kino_cms.dto.Page;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class GeneralPage extends Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PageType pageType;
    private String title;
    @Column(length = 5000)
    private String description;
    private String mainImage;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    @OneToOne(cascade = CascadeType.ALL)
    private SeoBlock seoBlock;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Boolean isActive;
    private String createTime;
    private Long translatePageId;

}
