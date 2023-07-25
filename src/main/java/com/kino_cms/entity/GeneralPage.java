package com.kino_cms.entity;

import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Data
@Entity
public class GeneralPage implements Page{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private PageType pageType;
    private String title;
    private String description;
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
    @CreationTimestamp
    private Timestamp createTime;

}
