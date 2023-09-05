package com.kino_cms.dto;

import com.kino_cms.enums.PageType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.sql.Timestamp;

public abstract class Page {
    private Long id;
    private String title;
    private Timestamp createTime ;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private PageType pageType;
}
