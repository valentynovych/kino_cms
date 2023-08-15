package com.kino_cms.repository;

import com.kino_cms.dto.BannerDTO;
import com.kino_cms.entity.Banner;
import com.kino_cms.entity.BannerImage;
import com.kino_cms.enums.BannerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BannerRepo extends JpaRepository<Banner, Long> {

    @Query("SELECT new com.kino_cms.dto.BannerDTO(b.id, b.bannerType, b.slideSpeed, " +
            "b.backgroundColor, b.isActivate) FROM Banner b WHERE b.bannerType = com.kino_cms.enums.BannerType.HEADER")
    Optional<BannerDTO> getBannerByTypeHeader();

    @Query("SELECT new com.kino_cms.dto.BannerDTO(b.id, b.bannerType, b.slideSpeed, " +
            "b.backgroundColor, b.isActivate) FROM Banner b WHERE b.bannerType = com.kino_cms.enums.BannerType.PERFORATING")
    Optional<BannerDTO> getBannerByTypePerforating();

    @Query("SELECT new com.kino_cms.dto.BannerDTO(b.id, b.bannerType, b.slideSpeed, " +
            "b.backgroundColor, b.isActivate) FROM Banner b WHERE b.bannerType = com.kino_cms.enums.BannerType.PROMOTION")
    Optional<BannerDTO> getBannerByTypePromotion();

    @Query("SELECT b.bannerImages FROM Banner b WHERE b.id =:bannerId")
    List<BannerImage> getImagesByBannerId(Long bannerId);
}
