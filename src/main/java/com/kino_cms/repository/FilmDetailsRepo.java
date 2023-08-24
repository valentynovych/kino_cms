package com.kino_cms.repository;

import com.kino_cms.entity.Film;
import com.kino_cms.entity.FilmDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FilmDetailsRepo extends JpaRepository<FilmDetails, Long> {

    @Query(value = "FROM FilmDetails fd WHERE fd.film =:filmId")
    Optional<FilmDetails> getFilmDetailsByFilmId(@Param("filmId") Film filmId);
}
