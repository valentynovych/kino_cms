package com.kino_cms.repository;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.entity.GeneralPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneralPageRepo extends JpaRepository<GeneralPage, Long> {

    @Query("SELECT new com.kino_cms.dto.GeneralPageDTO(gp.id, gp.pageType, gp.title,gp.description, " +
            "gp.mainImage, gp.image1, gp.image2, gp.image3, " +
            "gp.image4, gp.image5, gp.language, gp.isActive, " +
            "gp.createTime, gp.seoBlock.seoUrl, gp.seoBlock.seoTitle, gp.seoBlock.seoKeywords, " +
            "gp.seoBlock.seoDescription) FROM GeneralPage gp WHERE gp.id =:id")
    Optional<GeneralPageDTO> getGeneralPageById(@Param("id") Long id);
}
