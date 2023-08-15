package com.kino_cms.dto;

import com.kino_cms.entity.Banner;
import com.kino_cms.entity.BannerImage;
import com.kino_cms.enums.BannerType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BannerDTO {
    private Long id;
    @Enumerated(EnumType.STRING)
    private BannerType bannerType;
    private Integer slideSpeed;
    private String backgroundColor;
    private List<BannerImage> bannerImages;
    private Boolean isActivate;

    public BannerDTO(){

    }

    public BannerDTO(Long id, BannerType bannerType, Integer slideSpeed, String backgroundColor, Boolean isActivate) {
        this.id = id;
        this.bannerType = bannerType;
        this.slideSpeed = slideSpeed;
        this.backgroundColor = backgroundColor;
        this.isActivate = isActivate;
    }
}
