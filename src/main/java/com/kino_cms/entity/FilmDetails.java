package com.kino_cms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Time;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class FilmDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Film film;
    private Integer year;
    private String country;
    private String compositor;
    private String producer;
    private String scenarist;
    private String genre;
    private String budget;
    private Integer fromAge;
    private Integer duration;
}
