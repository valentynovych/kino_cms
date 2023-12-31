package com.kino_cms.repository;

import com.kino_cms.dto.FeedDTO;
import com.kino_cms.entity.FeedPage;
import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedPageRepo extends JpaRepository<FeedPage, Long> {
    List<FeedPage> findFeedPagesByFeedType(FeedType feedType);

    @Query("SELECT new com.kino_cms.dto.FeedDTO(fp.id, fp.feedType, fp.title, fp.description, fp.publishDate, " +
            "fp.mainImage, fp.image1, fp.image2, fp.image3, fp.image4, fp.image5, fp.urlVideo, fp.isActivate, " +
            "fp.language, fp.createTime, fp.seoBlock.id, fp.seoBlock.seoUrl, fp.seoBlock.seoTitle, " +
            "fp.seoBlock.seoKeywords, fp.seoBlock.seoDescription) " +
            "FROM  FeedPage fp WHERE fp.feedType = com.kino_cms.enums.FeedType.FEED AND fp.language =:language")
    List<FeedDTO> getAllFeedDTOByTypeFeed(Language language);

    @Query("SELECT new com.kino_cms.dto.FeedDTO(fp.id, fp.feedType, fp.title, fp.description, fp.publishDate, " +
            "fp.mainImage, fp.image1, fp.image2, fp.image3, fp.image4, fp.image5, fp.urlVideo, fp.isActivate, " +
            "fp.language, fp.createTime, fp.seoBlock.id, fp.seoBlock.seoUrl, fp.seoBlock.seoTitle, " +
            "fp.seoBlock.seoKeywords, fp.seoBlock.seoDescription) " +
            "FROM  FeedPage fp WHERE fp.feedType = com.kino_cms.enums.FeedType.PROMOTION AND fp.language =:language")
    List<FeedDTO> getAllFeedDTOByTypePromotion(Language language);

    @Query("SELECT new com.kino_cms.dto.FeedDTO(fp.id, fp.feedType, fp.title, fp.description, fp.publishDate, " +
            "fp.mainImage, fp.image1, fp.image2, fp.image3, fp.image4, fp.image5, fp.urlVideo, fp.isActivate, " +
            "fp.language, fp.createTime, fp.seoBlock.id, fp.seoBlock.seoUrl, fp.seoBlock.seoTitle, " +
            "fp.seoBlock.seoKeywords, fp.seoBlock.seoDescription) FROM  FeedPage fp WHERE fp.id =:feedId")
    FeedDTO getFeedDTOById(Long feedId);

    Optional<FeedPage> findByTranslatePageId(Long id);
}
