package com.kino_cms.service;

import com.kino_cms.dto.BannerDTO;
import com.kino_cms.entity.Banner;
import com.kino_cms.entity.BannerImage;
import com.kino_cms.enums.BannerType;
import com.kino_cms.repository.BannerImageRepo;
import com.kino_cms.repository.BannerRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class BannerService {

    private final BannerRepo bannerRepo;
    private final BannerImageRepo bannerImageRepo;

    public BannerDTO getHeaderBanner() {
        log.info("-> start execution method getHeaderBanner()");
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
            dto.setSlideSpeed(10);
        }
        List<BannerImage> bannerImages = dto.getBannerImages();
        int size = bannerImages.size();
        if (size < 5) {
            for (int i = 0; i < 5 - size; i++) {
                Banner banner = new Banner();
                banner.setId(dto.getId());
                BannerImage bannerImage = new BannerImage();
                bannerImage.setBanner(banner);
                bannerImages.add(bannerImage);
            }
        }
        bannerImages.stream().sorted((o1, o2) -> o1.getImage().compareToIgnoreCase(o2.getImage()));
        dto.setBannerImages(bannerImages);
        log.info("-> exit from method getHeaderBanner() with banner id: " + dto.getId());
        return dto;
    }

    public BannerDTO getPerforatingBanner() {
        log.info("-> start execution method getPerforatingBanner()");
        Optional<BannerDTO> dtoOptional = bannerRepo.getBannerByTypePerforating();
        BannerDTO dto;
        if (dtoOptional.isPresent()) {
            dto = dtoOptional.get();
            List<BannerImage> bannerImagesById = getBannerImagesById(dto.getId());
            if (bannerImagesById.isEmpty()) {
                BannerImage bannerImage = new BannerImage();
                bannerImage.setBanner(bannerRepo.findById(dto.getId()).get());
                bannerImagesById.add(bannerImage);
            }
            dto.setBannerImages(bannerImagesById);
        } else {
            dto = new BannerDTO();
            dto.setId(0L);
            dto.setBannerType(BannerType.PERFORATING);
            dto.setBannerImages(createEmptyList(1));
            dto.setSlideSpeed(10);
        }
        log.info("-> exit from method getPerforatingBanner() with banner id: " + dto.getId());
        return dto;
    }

    public BannerDTO getPromotionBanner() {
        log.info("-> start execution method getPromotionBanner()");
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
            dto.setSlideSpeed(10);
        }
        List<BannerImage> bannerImages = dto.getBannerImages();
        int size = bannerImages.size();
        if (size < 5) {
            for (int i = 0; i < 5 - size; i++) {
                Banner banner = new Banner();
                banner.setId(dto.getId());
                BannerImage bannerImage = new BannerImage();
                bannerImage.setBanner(banner);
                bannerImages.add(bannerImage);
            }
        }
        bannerImages.stream().sorted((o1, o2) -> o1.getImage().compareToIgnoreCase(o2.getImage()));
        dto.setBannerImages(bannerImages);
        log.info("-> exit from method getPromotionBanner() with banner id: " + dto.getId());
        return dto;
    }

    public List<BannerImage> getBannerImagesById(Long bannerId) {
        log.info(String.format("-> start execution method getBannerImagesById(bannerId %s)", bannerId));
        List<BannerImage> imagesByBannerId = bannerRepo.getImagesByBannerId(bannerId);
        log.info("-> exit from method getBannerImagesById() list size is: " + imagesByBannerId.size());
        return imagesByBannerId;
    }

    public Banner saveBannerDTO(BannerDTO dto) {
        log.info(String.format("-> start execution method saveBannerDTO(BannerType %s) ", dto.getBannerType()));
        Optional<Banner> bannerOptional = bannerRepo.findById(dto.getId());
        Banner banner;
        if (bannerOptional.isPresent()) {
            banner = bannerOptional.get();
        } else {
            banner = new Banner();
            banner.setBannerType(dto.getBannerType());
            banner.setId(0L);
        }

        List<BannerImage> bannerImages = dto.getBannerImages();
        for (BannerImage bannerImage : bannerImages) {
            if (bannerImage.getImage() != null && bannerImage.getImage().isEmpty()) {
                bannerImage = null;
            }
        }

        List<BannerImage> collect = bannerImages
                .stream()
                .filter(Objects::nonNull)
                .filter(bannerImage -> bannerImage.getImage() != null)
                .filter(bannerImage -> !bannerImage.getImage().isEmpty())
                .toList();
        banner.setSlideSpeed(dto.getSlideSpeed());
        banner.setBackgroundColor(dto.getBackgroundColor());
        banner.setIsActivate(dto.getIsActivate());
        collect.forEach(bannerImage -> bannerImage.setBanner(banner));
        banner.setBannerImages(collect);

        Banner save = bannerRepo.save(banner);
        log.info("-> exit from method saveBannerDTO()");
        return save;
    }

    private List<BannerImage> createEmptyList(Integer capacity) {
        log.info(String.format("-> start execution private method createEmptyList(capacity %s)", capacity));
        List<BannerImage> bannerImages = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            bannerImages.add(new BannerImage());
        }
        log.info("-> exit from private method createEmptyList() list size is: " + bannerImages.size());
        return bannerImages;
    }

    public List<String> getListImagesFileNameByBannerType(BannerType bannerType) {
        log.info(String.format("-> start execution method getListImagesFileNameById(BannerType %s)", bannerType));
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
        log.info("-> exit from method getListImagesFileNameById() list size is: " + fileNamesFromDB.size());
        return fileNamesFromDB;
    }

    public BannerDTO updateImagesOnModel(BannerDTO bannerDTO, List<String> fileNamesFromDB) {
        log.info(String.format("-> start execution private method updateImagesOnModel(bannerId %s)", bannerDTO.getId()));
        List<BannerImage> bannerImages = bannerDTO.getBannerImages();
        for (int i = 0; i < bannerImages.size(); i++) {
            BannerImage get = bannerImages.get(i);
            get.setImage(fileNamesFromDB.get(i));
        }
        log.info("-> exit from method updateImagesOnModel() list size is: " + bannerDTO.getBannerImages().size());
        return bannerDTO;
    }

    public void deleteEmptyBannerImage(ArrayList<MultipartFile> images, BannerDTO bannerDTOModel) {
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).getOriginalFilename().equals("empty.png")
                    && bannerDTOModel.getBannerImages().get(i).getId() != null) {
                Optional<BannerImage> bannerImageOptional = bannerImageRepo.findById(bannerDTOModel.getBannerImages().get(i).getId());
                BannerImage bannerImage = bannerImageOptional.get();
                bannerImageRepo.delete(bannerImage);

            }
        }
    }
}