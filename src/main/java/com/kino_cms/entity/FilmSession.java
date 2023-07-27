package com.kino_cms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
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
}
