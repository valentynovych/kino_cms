package com.kino_cms.repository;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.entity.GeneralPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GeneralPageRepo extends JpaRepository<GeneralPage, Long> {

    @Query("SELECT NEW com.kino_cms.dto.GeneralPageDTO(gep.id, gep.pageType, gep.title, gep.description, " +
            "gep.mainImage, gep.image1, gep.image2, gep.image3, " +
            "gep.image4, gep.image5, gep.language, gep.isActive, " +
            "gep.createTime, gep.seoBlock.seoUrl, gep.seoBlock.seoTitle, gep.seoBlock.seoKeywords, " +
            "gep.seoBlock.seoDescription) FROM GeneralPage gep WHERE gep.id =:id")
    Optional<GeneralPageDTO> getGeneralPageDTOById(@Param("id") Long id);
}
