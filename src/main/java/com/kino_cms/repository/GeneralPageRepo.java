package com.kino_cms.repository;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.dto.Page;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.enums.PageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneralPageRepo extends JpaRepository<GeneralPage, Long> {

    @Query("SELECT NEW com.kino_cms.dto.GeneralPageDTO(gep.id, gep.pageType, gep.title, gep.description, " +
            "gep.mainImage, gep.image1, gep.image2, gep.image3, " +
            "gep.image4, gep.image5, gep.language, gep.isActive, " +
            "gep.createTime, gep.seoBlock.seoUrl, gep.seoBlock.seoTitle, gep.seoBlock.seoKeywords, " +
            "gep.seoBlock.seoDescription) FROM GeneralPage gep WHERE gep.id =:id")
    Optional<GeneralPageDTO> getGeneralPageDTOById(@Param("id") Long id);

    @Query("SELECT NEW com.kino_cms.dto.GeneralPageDTO(gep.id, gep.pageType, gep.title, gep.description, " +
            "gep.mainImage, gep.image1, gep.image2, gep.image3, " +
            "gep.image4, gep.image5, gep.language, gep.isActive, " +
            "gep.createTime, gep.seoBlock.seoUrl, gep.seoBlock.seoTitle, gep.seoBlock.seoKeywords, " +
            "gep.seoBlock.seoDescription) FROM GeneralPage gep WHERE gep.pageType = com.kino_cms.enums.PageType.OTHER_PAGE")
    List<GeneralPageDTO> getAllByPageTypeOtherPage();

    @Query(value = "FROM GeneralPage gp WHERE gp.pageType = com.kino_cms.enums.PageType.ADVERTISING " +
            "AND gp.language = com.kino_cms.enums.Language.UKRAINIAN " +
            "UNION FROM GeneralPage gp WHERE gp.pageType = com.kino_cms.enums.PageType.CAFE_BAR " +
            "AND gp.language = com.kino_cms.enums.Language.UKRAINIAN " +
            "UNION FROM GeneralPage gp WHERE gp.pageType = com.kino_cms.enums.PageType.CONTACT_PAGE " +
            "AND gp.language = com.kino_cms.enums.Language.UKRAINIAN " +
            "UNION FROM GeneralPage gp WHERE gp.pageType = com.kino_cms.enums.PageType.CHILD_ROOM " +
            "AND gp.language = com.kino_cms.enums.Language.UKRAINIAN " +
            "UNION FROM GeneralPage gp WHERE gp.pageType = com.kino_cms.enums.PageType.VIP_HALL " +
            "AND gp.language = com.kino_cms.enums.Language.UKRAINIAN")
    List<GeneralPage> getAllUkPageByPageTypeUnion();
}
