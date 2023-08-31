package com.kino_cms.repository;

import com.kino_cms.entity.Film;
import com.kino_cms.entity.FilmSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmSessionRepo extends JpaRepository<FilmSession, Long> {
    @Query(value = "FROM FilmSession fs WHERE fs.film =:film")
    List<FilmSession> getFilmSessionByFilm(Film film);

    @Query("FROM FilmSession fs WHERE fs.cinema.id =:cinemaId")
    List<FilmSession> getFilmSessionByCinema(Long cinemaId);
}
