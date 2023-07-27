package com.kino_cms.entity;

import com.kino_cms.enums.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@NoArgsConstructor
@Data
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
}
