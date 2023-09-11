package com.kino_cms.repository;

import com.kino_cms.dto.HallDTO;
import com.kino_cms.entity.Cinema;
import com.kino_cms.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepo extends JpaRepository<Hall, Long> {
    @Query("SELECT new com.kino_cms.dto.HallDTO(h.id, h.name, h.descriptions, h.hallSchema, h.firstBanner, " +
            "h.image1, h.image2, h.image3, h.image4, h.image5, h.language, h.createTime, h.cinema.id, h.seoBlock.seoUrl, " +
            "h.seoBlock.seoTitle, h.seoBlock.seoKeywords, h.seoBlock.seoDescription, h.translatePageId) " +
            "FROM Hall h WHERE h.id =:id")
    Optional<HallDTO> getHallDtoById(@Param("id") Long id);

    @Query("SELECT new com.kino_cms.dto.HallDTO(h.id, h.name, h.descriptions, h.hallSchema, h.firstBanner, " +
            "h.image1, h.image2, h.image3, h.image4, h.image5, h.language, h.createTime, h.cinema.id, h.seoBlock.seoUrl, " +
            "h.seoBlock.seoTitle, h.seoBlock.seoKeywords, h.seoBlock.seoDescription, h.translatePageId) " +
            "FROM Hall h WHERE h.cinema =:cinema")
    List<HallDTO> getAllHallByCinema(@Param("cinema") Cinema cinema);

    @Query("SELECT new com.kino_cms.dto.HallDTO(h.id, h.name, h.descriptions, h.hallSchema, h.firstBanner, " +
            "h.image1, h.image2, h.image3, h.image4, h.image5, h.language, h.createTime, h.cinema.id, h.seoBlock.seoUrl, " +
            "h.seoBlock.seoTitle, h.seoBlock.seoKeywords, h.seoBlock.seoDescription, h.translatePageId) " +
            "FROM Hall h WHERE h.translatePageId =:id")
    Optional<HallDTO> findByTranslatePageId(Long id);
}
