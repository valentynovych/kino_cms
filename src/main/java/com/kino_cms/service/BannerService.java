package com.kino_cms.service;

import com.kino_cms.dto.BannerDTO;
import com.kino_cms.entity.Banner;
import com.kino_cms.entity.BannerImage;
import com.kino_cms.enums.BannerType;
import com.kino_cms.repository.BannerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BannerService {
    @Autowired
    BannerRepo bannerRepo;

    public BannerDTO getHeaderBanner() {
        Optional<BannerDTO> dtoOptional = bannerRepo.getBannerByTypeHeader();
        BannerDTO dto;
        if (dtoOptional.isPresent()) {
            dto = dtoOptional.get();
            dto.setBannerImages(getBannerImagesById(dto.getId()));
        } else {
            dto = new BannerDTO();
            dto.setId(0L);
            dto.setBannerType(BannerType.HEADER);
            dto.setBannerImages(createEmptyList(5));
        }
        return dto;
    }

    public BannerDTO getPerforatingBanner() {
        Optional<BannerDTO> dtoOptional = bannerRepo.getBannerByTypePerforating();
        BannerDTO dto;
        if (dtoOptional.isPresent()) {
            dto = dtoOptional.get();
            dto.setBannerImages(getBannerImagesById(dto.getId()));
        } else {
            dto = new BannerDTO();
            dto.setId(0L);
            dto.setBannerType(BannerType.PERFORATING);
            dto.setBannerImages(createEmptyList(1));
        }
        return dto;
    }

    public BannerDTO getPromotionBanner() {
        Optional<BannerDTO> dtoOptional = bannerRepo.getBannerByTypePromotion();
        BannerDTO dto;
        if (dtoOptional.isPresent()) {
            dto = dtoOptional.get();
            dto.setBannerImages(getBannerImagesById(dto.getId()));
        } else {
            dto = new BannerDTO();
            dto.setId(0L);
            dto.setBannerType(BannerType.PROMOTION);
            dto.setBannerImages(createEmptyList(5));
        }
        return dto;
    }

    public List<BannerImage> getBannerImagesById(Long bannerId) {
        return bannerRepo.getImagesByBannerId(bannerId);
    }

    public void saveBannerDTO(BannerDTO dto) {

        Optional<Banner> bannerOptional = bannerRepo.findById(dto.getId());
        Banner banner;
        if (bannerOptional.isPresent()) {
            banner = bannerOptional.get();
        } else {
            banner = new Banner();
            banner.setBannerType(dto.getBannerType());
        }

        banner.setSlideSpeed(dto.getSlideSpeed());
        banner.setBackgroundColor(dto.getBackgroundColor());
        banner.setIsActivate(dto.getIsActivate());
        banner.setBannerImages(dto.getBannerImages());

        bannerRepo.save(banner);
    }

    private List<BannerImage> createEmptyList(Integer capacity) {
        List<BannerImage> bannerImages = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            bannerImages.add(new BannerImage());
        }
        return bannerImages;
    }

    public List<String> getListImagesFileNameById(BannerType bannerType) {
        List<String> fileNamesFromDB;
        BannerDTO bannerDTO = null;
        if (bannerType.equals(BannerType.HEADER)) {
            bannerDTO = getHeaderBanner();
        } else if (bannerType.equals(BannerType.PERFORATING)) {
            bannerDTO = getPerforatingBanner();
        } else if (bannerType.equals(BannerType.PROMOTION)) {
            bannerDTO = getPromotionBanner();
        }

        fileNamesFromDB = bannerDTO.getBannerImages().stream().map(BannerImage::getImage).toList();

        return fileNamesFromDB;
    }

    public BannerDTO updateImagesOnModel(BannerDTO bannerDTO, List<String> fileNamesFromDB) {
        List<BannerImage> bannerImages = bannerDTO.getBannerImages();
        for (int i = 0; i < bannerImages.size(); i++) {
            BannerImage get = bannerImages.get(i);
            get.setImage(fileNamesFromDB.get(i));
        }
        return bannerDTO;
    }
}