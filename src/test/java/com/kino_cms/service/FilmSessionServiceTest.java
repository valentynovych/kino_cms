package com.kino_cms.service;

import com.kino_cms.entity.Film;
import com.kino_cms.entity.FilmSession;
import com.kino_cms.enums.FilmType;
import com.kino_cms.repository.FilmSessionRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class FilmSessionServiceTest {

    @Mock
    FilmSessionRepo filmSessionRepo;
    @Mock
    FilmService filmService;
    FilmSessionService filmSessionService;
    private Film film;
    private FilmSession filmSession;

    @BeforeEach
    void setUp() {
        filmSessionService = new FilmSessionService(filmSessionRepo, filmService);
        film = new Film();
        film.setId(1L);
        filmSession = new FilmSession();
        filmSession.setFilm(film);
        filmSession.setFilmType(FilmType.D2);
        filmSession.setSessionName("session1");
        filmSession.setDateTime(LocalDateTime.now());
        filmSession.setNumberOfTickets(50);
    }

    @Test
    void getAllSessionByFilmId() {
        when(filmService.getFilmById(1L)).thenReturn(Optional.ofNullable(film));
        when(filmSessionRepo.getFilmSessionByFilm(film)).thenReturn(List.of(filmSession, new FilmSession()));
        when(filmService.getFilmById(2L)).thenReturn(Optional.empty());

        List<FilmSession> allSessionByFilmId = filmSessionService.getAllSessionByFilmId(film.getId());
        assertFalse(allSessionByFilmId.isEmpty());
        assertTrue(allSessionByFilmId.size() > 1);
        assertEquals(filmSession.getFilmType(), allSessionByFilmId.get(0).getFilmType());

        List<FilmSession> allSessionByFilmId1 = filmSessionService.getAllSessionByFilmId(2L);
        assertTrue(allSessionByFilmId1.isEmpty());

    }

    @Test
    void getAllSessionByCinemaId() {
        when(filmSessionRepo.getFilmSessionByCinema(1L)).thenReturn(List.of(filmSession, new FilmSession()));
        List<FilmSession> allSessionByCinemaId = filmSessionService.getAllSessionByCinemaId(1L);
        assertFalse(allSessionByCinemaId.isEmpty());
        assertTrue(allSessionByCinemaId.size() > 1);
        assertEquals(filmSession.getDateTime(), allSessionByCinemaId.get(0).getDateTime());
    }

    @Test
    void getAllSession() {
        when(filmSessionRepo.findAll()).thenReturn(List.of(filmSession, new FilmSession(), new FilmSession()));
        List<FilmSession> allSessionByCinemaId = filmSessionService.getAllSession();
        assertFalse(allSessionByCinemaId.isEmpty());
        assertTrue(allSessionByCinemaId.size() > 1);
        assertEquals(filmSession.getDateTime(), allSessionByCinemaId.get(0).getDateTime());
    }

    @Test
    void getSessionById() {
        when(filmSessionRepo.findById(1L)).thenReturn(Optional.of(filmSession));
        when(filmSessionRepo.findById(2L)).thenReturn(Optional.empty());

        FilmSession sessionById = filmSessionService.getSessionById(1L);
        assertNotNull(sessionById);
        assertEquals(filmSession.getFilm(), sessionById.getFilm());
        assertEquals(filmSession.getSessionName(), sessionById.getSessionName());

        FilmSession sessionById1 = filmSessionService.getSessionById(2L);
        assertNotNull(sessionById1);
        assertNull(sessionById1.getSessionName());
        assertNull(sessionById1.getFilm());
    }
}