package com.kino_cms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class ContactCinema {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private ContactPage contactPage;
    private String title;
    private String address;
    private String coordinates;
    private String logo_image;
    private Boolean isActivate;
}
