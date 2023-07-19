package com.kino_cms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class SeoBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String seoUrl;
    private String seoTitle;
    private String seoKeywords;
    private String seoDescription;

}
