package com.kino_cms.entity;

import com.kino_cms.enums.FilmType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class FilmSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Film film;
    @ManyToOne(cascade = CascadeType.ALL)
    private Cinema cinema;
    @ManyToOne(cascade = CascadeType.ALL)
    private Hall hall;
    private LocalDateTime dateTime;
    private Integer price;
    private Integer numberOfTickets;
    @OneToMany(mappedBy = "filmSession", cascade = CascadeType.ALL)
    private List<Ticket> ticketList;
    @Enumerated(EnumType.STRING)
    private FilmType filmType;
    private String sessionName;
}
