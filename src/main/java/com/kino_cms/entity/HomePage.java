package com.kino_cms.entity;


import com.kino_cms.dto.Page;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class HomePage extends Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PageType pageType;
    private String title;
    private String phone_main;
    private String phone_other;
    @Column(length = 2000)
    private String seoText;
    @OneToOne(cascade = CascadeType.ALL)
    private SeoBlock seoBlock;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Boolean isActive;
    private String createTime;
}
