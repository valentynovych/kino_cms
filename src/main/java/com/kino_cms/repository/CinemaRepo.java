package com.kino_cms.repository;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.entity.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CinemaRepo extends JpaRepository<Cinema, Long> {

    @Query("SELECT new com.kino_cms.dto.CinemaDTO(c.id, c.name, c.description, c.conditions, c.logoImage, " +
            "c.firstBanner, c.image1, c.image2, c.image3, c.image4, c.image5, c.language, c.seoBlock.seoUrl, " +
            "c.seoBlock.seoTitle, c.seoBlock.seoKeywords, c.seoBlock.seoDescription, c.translatePageId) " +
            "FROM Cinema c WHERE c.id =:id")
    Optional<CinemaDTO> getCinemaDtoById(@Param("id") Long id);

    @Query("SELECT new com.kino_cms.dto.CinemaDTO(c.id, c.name, c.description, c.conditions, c.logoImage, " +
            "c.firstBanner, c.image1, c.image2, c.image3, c.image4, c.image5, c.language, c.seoBlock.seoUrl, " +
            "c.seoBlock.seoTitle, c.seoBlock.seoKeywords, c.seoBlock.seoDescription, c.translatePageId) " +
            "FROM Cinema c")
    List<CinemaDTO> getAllCinemaDto();
    @Query("SELECT new com.kino_cms.dto.CinemaDTO(c.id, c.name, c.description, c.conditions, c.logoImage, " +
            "c.firstBanner, c.image1, c.image2, c.image3, c.image4, c.image5, c.language, c.seoBlock.seoUrl, " +
            "c.seoBlock.seoTitle, c.seoBlock.seoKeywords, c.seoBlock.seoDescription, c.translatePageId) " +
            "FROM Cinema c WHERE c.translatePageId =:id")
    Optional<CinemaDTO> getCinemaDtoByTranslatePageId(Long id);
}
