package com.kino_cms.entity;


import com.kino_cms.dto.Page;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
public class HomePage implements Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PageType pageType;
    private String title;
    private String phone_main;
    private String phone_other;
    private String seoText;
    @OneToOne(cascade = CascadeType.ALL)
    private SeoBlock seoBlock;
    @Enumerated(EnumType.STRING)
    private Language language;
    private Boolean isActive;
    private String createTime;
}
