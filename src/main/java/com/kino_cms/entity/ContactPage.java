package com.kino_cms.entity;

import com.kino_cms.enums.Language;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class ContactPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    private SeoBlock seoBlock;
    @OneToMany(mappedBy ="contactPage", cascade = CascadeType.ALL)
    private List<ContactCinema> contactCinemaList;
    @Enumerated(EnumType.STRING)
    private Language language;
}
