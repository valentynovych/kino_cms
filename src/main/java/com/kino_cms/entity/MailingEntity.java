package com.kino_cms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@Entity
public class MailingEntity {
    @Id
    private Long id;
    private String lastTemplates;
    private String lastUsedTemplate;
    private Integer countMails;
}
