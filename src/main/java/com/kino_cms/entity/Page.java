package com.kino_cms.entity;

import com.kino_cms.enums.PageType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.sql.Timestamp;

public interface Page {
    Long id = null;
    String title = null;
    Timestamp createTime = null;
    Boolean isActive = null;
    @Enumerated(EnumType.STRING)
    PageType pageType = null;
}
