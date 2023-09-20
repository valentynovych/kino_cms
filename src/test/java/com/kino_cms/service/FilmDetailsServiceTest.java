package com.kino_cms.service;

import com.kino_cms.entity.Film;
import com.kino_cms.entity.FilmDetails;
import com.kino_cms.repository.FilmDetailsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilmDetailsServiceTest {

    @Mock
    FilmDetailsRepo filmDetailsRepo;
    @Mock
    FilmService filmService;
    FilmDetailsService filmDetailsService;
    private FilmDetails filmDetails;
    private Film film;

    @BeforeEach
    void setUp() {
        filmDetailsService = new FilmDetailsService(filmDetailsRepo, filmService);
        filmDetails = new FilmDetails();
        film = new Film();
        film.setId(1L);
        filmDetails.setFilm(film);
        filmDetails.setId(1L);
        filmDetails.setBudget("2000000");
        filmDetails.setCountry("country");
        filmDetails.setDuration(120);
        filmDetails.setCompositor("compositor");
        filmDetails.setGenre("genre");
        filmDetails.setProducer("producer");
        filmDetails.setScenarist("scenarist");
        filmDetails.setFromAge(12);
        filmDetails.setYear(2020);
    }

    @Test
    void getFilmDetailsByFilmId() {
        when(filmService.getFilmById(filmDetails.getFilm().getId())).thenReturn(Optional.of(film));
        when(filmDetailsRepo.getFilmDetailsByFilmId(ArgumentMatchers.any(Film.class)))
                .thenReturn(Optional.of(filmDetails));

        FilmDetails filmDetailsByFilmId = filmDetailsService.getFilmDetailsByFilmId(film.getId());
        assertNotNull(filmDetailsByFilmId);
        assertEquals(filmDetails.getDuration(), filmDetailsByFilmId.getDuration());
        assertEquals(filmDetails.getYear(), filmDetailsByFilmId.getYear());
        assertEquals(filmDetails.getFromAge(), filmDetailsByFilmId.getFromAge());
        assertEquals(filmDetails.getCompositor(), filmDetailsByFilmId.getCompositor());

        film.setId(2L);
        filmDetails.setId(2L);
        when(filmService.getFilmById(filmDetails.getFilm().getId())).thenReturn(Optional.empty());
        when(filmDetailsRepo.getFilmDetailsByFilmId(ArgumentMatchers.any(Film.class))).thenReturn(Optional.empty());

        FilmDetails filmDetailsByFilmId1 = filmDetailsService.getFilmDetailsByFilmId(film.getId());
        assertNotNull(filmDetailsByFilmId1);
        assertEquals(filmDetails.getFilm().getId(), filmDetailsByFilmId1.getFilm().getId());
    }

    @Test
    void saveFilmDetails() {
        filmDetailsService.saveFilmDetails(filmDetails);
    }
}