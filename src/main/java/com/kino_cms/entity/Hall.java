package com.kino_cms.entity;

import com.kino_cms.enums.Language;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Cinema cinema;
    private String name;
    private String descriptions;
    private String hallSchema; //image
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
    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
    private List<FilmSession> filmSessions;
    private String createTime;
    private Long translatePageId;
}
