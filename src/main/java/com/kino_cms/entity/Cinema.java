package com.kino_cms.entity;

import com.kino_cms.enums.Language;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Cinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 3000)
    private String description;
    @Column(length = 3000)
    private String conditions;
    private String logoImage;
    private String firstBanner;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    @OneToOne(cascade = CascadeType.ALL)
    private SeoBlock seoBlock;
    @Enumerated(EnumType.STRING)
    private Language language;
    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<Hall> hallList;
    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private List<FilmSession> filmSessions;
    private Long translatePageId;
}
