package com.kino_cms.entity;

import com.kino_cms.enums.FilmType;
import com.kino_cms.enums.Language;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 2000)
    private String description;
    private String mainImage;
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    private String urlVideo;
    @OneToOne(cascade = CascadeType.ALL)
    private SeoBlock seoBlock;
    @Enumerated(EnumType.STRING)
    private Language language;
    @ElementCollection(targetClass = FilmType.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "film_types", joinColumns = @JoinColumn(name = "film_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "film_type_id")
    private List<FilmType> filmTypeList;
    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL)
    private List<FilmSession> filmSessions;
    private Date dateOfPremiere;
    private Date dateEndPremiere;
    private String datePremiereFromTo;
    private Long translatePageId;
}
