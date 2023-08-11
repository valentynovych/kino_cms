package com.kino_cms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;

@NoArgsConstructor
@Data
@Entity
public class FilmDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL)
    private Film film;
    private Integer year;
    private String country;
    private String compositor;
    private String producer;
    private String scenarist;
    private String genre;
    private String budget;
    private Integer fromAge;
    private Time duration;
}
