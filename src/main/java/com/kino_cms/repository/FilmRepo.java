package com.kino_cms.repository;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepo extends JpaRepository<Film, Long> {

    @Query("SELECT new com.kino_cms.dto.FilmDTO(f.id, f.name, f.description, f.mainImage, f.image1, f.image2, f.image3, " +
            "f.image4, f.image5, f.urlVideo, f.language, f.seoBlock.seoUrl, f.seoBlock.seoTitle, f.seoBlock.seoKeywords, " +
            "f.seoBlock.seoDescription, f.dateOfPremiere, f.dateEndPremiere, f.datePremiereFromTo) FROM Film f WHERE f.id =:id")
    Optional<FilmDTO> getFilmDtoById(@Param("id") Long id);

    @Query("SELECT new com.kino_cms.dto.FilmDTO(f.id, f.name, f.description, f.mainImage, f.image1, f.image2, f.image3, " +
            "f.image4, f.image5, f.urlVideo, f.language, f.seoBlock.seoUrl, f.seoBlock.seoTitle, f.seoBlock.seoKeywords, " +
            "f.seoBlock.seoDescription, f.dateOfPremiere, f.dateEndPremiere, f.datePremiereFromTo) FROM Film f")
    List<FilmDTO> getAllFilmDto();

    @Query("SELECT new com.kino_cms.dto.FilmDTO(f.id, f.name, f.description, f.mainImage, f.image1, f.image2, f.image3, " +
            "f.image4, f.image5, f.urlVideo, f.language, f.seoBlock.seoUrl, f.seoBlock.seoTitle, f.seoBlock.seoKeywords, " +
            "f.seoBlock.seoDescription, f.dateOfPremiere, f.dateEndPremiere, f.datePremiereFromTo) " +
            "FROM Film f WHERE now() >= f.dateOfPremiere AND  now() <= f.dateEndPremiere")
    List<FilmDTO> getAllFilmIsReleasedNow();

    @Query("SELECT new com.kino_cms.dto.FilmDTO(f.id, f.name, f.description, f.mainImage, f.image1, f.image2, f.image3, " +
            "f.image4, f.image5, f.urlVideo, f.language, f.seoBlock.seoUrl, f.seoBlock.seoTitle, f.seoBlock.seoKeywords,  " +
            "f.seoBlock.seoDescription, f.dateOfPremiere, f.dateEndPremiere, f.datePremiereFromTo) " +
            "FROM Film f WHERE f.dateOfPremiere > now()")
    List<FilmDTO> getAllFilmReleasedSoon();
}
