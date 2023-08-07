package com.kino_cms.entity;

import com.kino_cms.dto.Page;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import org.hibernate.annotations.Parent;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ContactPage implements Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private PageType pageType;
    @OneToOne(cascade = CascadeType.ALL)
    private SeoBlock seoBlock;
    @OneToMany(mappedBy ="contactPage", cascade = CascadeType.ALL)
    private List<ContactCinema> contactCinemaList;
    @Enumerated(EnumType.STRING)
    private Language language;
    private String createTime;
    private Boolean isActive;
}
