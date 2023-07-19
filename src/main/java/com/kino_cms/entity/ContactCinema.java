package com.kino_cms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
public class ContactCinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private ContactPage contactPage;
    private String title;
    private String address;
    private String coordinates;
    private String logo_image;
    private Boolean isActivate;
}
