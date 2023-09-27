package com.kino_cms.service;

import com.kino_cms.dto.BannerDTO;
import com.kino_cms.entity.Banner;
import com.kino_cms.entity.BannerImage;
import com.kino_cms.enums.BannerType;
import com.kino_cms.repository.BannerRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BannerServiceTest {

    @Mock
    private BannerRepo bannerRepo;
    private BannerService bannerService;

    @BeforeEach
    void setUp() {
        bannerService = new BannerService(bannerRepo);

    }

    @Test
    void getHeaderBanner() {
        when(bannerRepo.getBannerByTypeHeader()).thenReturn(Optional.empty());
        BannerDTO fromService = bannerService.getHeaderBanner();
        assertEquals(0L, fromService.getId());
        assertEquals(BannerType.HEADER, fromService.getBannerType());
        assertFalse(fromService.getBannerImages().isEmpty());

        BannerDTO headerBanner = new BannerDTO(
                1L,
                BannerType.HEADER,
                4,
                "RED",
                true);

        when(bannerRepo.getBannerByTypeHeader()).thenReturn(Optional.of(headerBanner));
        BannerDTO bannerDTO = bannerService.getHeaderBanner();
        assertEquals(1L, bannerDTO.getId());
        assertEquals(BannerType.HEADER, bannerDTO.getBannerType());

    }

    @Test
    void getPerforatingBanner() {
        when(bannerRepo.getBannerByTypePerforating()).thenReturn(Optional.empty());

        BannerDTO fromService = bannerService.getPerforatingBanner();
        assertEquals(0L, fromService.getId());
        assertEquals(BannerType.PERFORATING, fromService.getBannerType());
        assertFalse(fromService.getBannerImages().isEmpty());

        BannerDTO perforationBanner = new BannerDTO(
                1L,
                BannerType.PERFORATING,
                4,
                "RED",
                true);

        when(bannerRepo.getBannerByTypePerforating()).thenReturn(Optional.of(perforationBanner));
        Banner banner = new Banner();
        banner.setId(perforationBanner.getId());
        when(bannerRepo.findById(perforationBanner.getId())).thenReturn(Optional.of(banner));
        BannerDTO bannerDTO = bannerService.getPerforatingBanner();
        assertEquals(1L, bannerDTO.getId());
        assertEquals(BannerType.PERFORATING, bannerDTO.getBannerType());
    }

    @Test
    void getPromotionBanner() {
        when(bannerRepo.getBannerByTypePromotion()).thenReturn(Optional.empty());
        BannerDTO fromService = bannerService.getPromotionBanner();
        assertEquals(0L, fromService.getId());
        assertEquals(BannerType.PROMOTION, fromService.getBannerType());
        assertFalse(fromService.getBannerImages().isEmpty());

        BannerDTO promotionBanner = new BannerDTO(
                1L,
                BannerType.PROMOTION,
                4,
                "RED",
                true);

        when(bannerRepo.getBannerByTypePromotion()).thenReturn(Optional.of(promotionBanner));
        BannerDTO bannerDTO = bannerService.getPromotionBanner();
        assertEquals(1L, bannerDTO.getId());
        assertEquals(BannerType.PROMOTION, bannerDTO.getBannerType());
    }

    @Test
    void getBannerImagesById() {

        when(bannerRepo.getImagesByBannerId(0L))
                .thenReturn(new ArrayList<>());
        when(bannerRepo.getImagesByBannerId(1L))
                .thenReturn(List.of(new BannerImage(),
                        new BannerImage(),
                        new BannerImage()));

        List<BannerImage> bannerImagesById = bannerService.getBannerImagesById(0L);
        assertTrue(bannerImagesById.isEmpty());

        List<BannerImage> bannerImagesById1 = bannerService.getBannerImagesById(1L);
        assertFalse(bannerImagesById1.isEmpty());
    }

    @Test
    void saveBannerDTO_WhenBannerIsEmpty() {
        when(bannerRepo.findById(0L)).thenReturn(Optional.empty());
        BannerDTO bannerDTO = new BannerDTO(
                0L,
                BannerType.PROMOTION,
                4,
                "RED",
                true);
        bannerDTO.setBannerImages(List.of(new BannerImage(), new BannerImage(),
                new BannerImage(), new BannerImage(), new BannerImage()));
        Banner bannerForReturn = new Banner();
        bannerForReturn.setBannerType(bannerDTO.getBannerType());
        bannerForReturn.setId(bannerDTO.getId());
        bannerForReturn.setSlideSpeed(bannerDTO.getSlideSpeed());
        bannerForReturn.setBannerImages(bannerDTO.getBannerImages());
        bannerForReturn.setIsActivate(bannerDTO.getIsActivate());
        bannerForReturn.setBackgroundColor(bannerDTO.getBackgroundColor());

        when(bannerRepo.save(ArgumentMatchers.any(Banner.class)))
                .thenReturn(bannerForReturn);
        Banner banner = bannerService.saveBannerDTO(bannerDTO);

        Assertions.assertEquals(bannerDTO.getId(), banner.getId());
        Assertions.assertEquals(bannerDTO.getBannerImages().size(), banner.getBannerImages().size());
        Assertions.assertEquals(bannerDTO.getBannerType(), banner.getBannerType());
        Assertions.assertEquals(bannerDTO.getSlideSpeed(), banner.getSlideSpeed());
        Assertions.assertEquals(bannerDTO.getIsActivate(), banner.getIsActivate());
    }
    @Test
    void saveBannerDTO_WhenBannerIsPresent() {
        BannerDTO bannerDTO = new BannerDTO(
                1L,
                BannerType.PROMOTION,
                4,
                "RED",
                true);
        bannerDTO.setBannerImages(List.of(new BannerImage(), new BannerImage(),
                new BannerImage(), new BannerImage(), new BannerImage()));
        Banner bannerForReturn = new Banner();
        bannerForReturn.setBannerType(bannerDTO.getBannerType());
        bannerForReturn.setId(bannerDTO.getId());
        bannerForReturn.setSlideSpeed(bannerDTO.getSlideSpeed());
        bannerForReturn.setBannerImages(bannerDTO.getBannerImages());
        bannerForReturn.setIsActivate(bannerDTO.getIsActivate());
        bannerForReturn.setBackgroundColor(bannerDTO.getBackgroundColor());

        when(bannerRepo.findById(1L)).thenReturn(Optional.of(bannerForReturn));
        when(bannerRepo.save(ArgumentMatchers.any(Banner.class)))
                .thenReturn(bannerForReturn);

        Banner banner = bannerService.saveBannerDTO(bannerDTO);

        Assertions.assertEquals(bannerDTO.getId(), banner.getId());
        Assertions.assertEquals(bannerDTO.getBannerImages().size(), banner.getBannerImages().size());
        Assertions.assertEquals(bannerDTO.getBannerType(), banner.getBannerType());
        Assertions.assertEquals(bannerDTO.getSlideSpeed(), banner.getSlideSpeed());
        Assertions.assertEquals(bannerDTO.getIsActivate(), banner.getIsActivate());
    }


    @Test
    void getListImagesFileNameByBannerType_HEADER() {
        BannerDTO bannerDTO = new BannerDTO(
                1L,
                BannerType.HEADER,
                4,
                "RED",
                true);
        BannerImage bannerImage1 = new BannerImage();
        bannerImage1.setImage("ImageName1");
        BannerImage bannerImage2 = new BannerImage();
        bannerImage2.setImage("ImageName2");
        BannerImage bannerImage3 = new BannerImage();
        bannerImage3.setImage("ImageName3");
        BannerImage bannerImage4 = new BannerImage();
        bannerImage4.setImage("ImageName4");
        BannerImage bannerImage5 = new BannerImage();
        bannerImage5.setImage("ImageName5");
        bannerDTO.setBannerImages(List.of(bannerImage1, bannerImage2,
                bannerImage3, bannerImage4, bannerImage4));

        when(bannerRepo.getBannerByTypeHeader()).thenReturn(Optional.of(bannerDTO));
        when(bannerRepo.getImagesByBannerId(1L)).thenReturn(bannerDTO.getBannerImages());

        List<String> listImagesFileNameById = bannerService.getListImagesFileNameByBannerType(BannerType.HEADER);
        List<String> list = bannerDTO.getBannerImages().stream().map(BannerImage::getImage).toList();

        Assertions.assertFalse(listImagesFileNameById.isEmpty());
        Assertions.assertTrue(list.containsAll(listImagesFileNameById));

    }

    @Test
    void getListImagesFileNameByBannerType_PERFORATING() {
        BannerDTO bannerDTO = new BannerDTO(
                1L,
                BannerType.PERFORATING,
                4,
                "RED",
                true);
        BannerImage bannerImage1 = new BannerImage();
        bannerImage1.setImage("ImageName1");
        BannerImage bannerImage2 = new BannerImage();
        bannerImage2.setImage("ImageName2");
        BannerImage bannerImage3 = new BannerImage();
        bannerImage3.setImage("ImageName3");
        BannerImage bannerImage4 = new BannerImage();
        bannerImage4.setImage("ImageName4");
        BannerImage bannerImage5 = new BannerImage();
        bannerImage5.setImage("ImageName5");
        bannerDTO.setBannerImages(List.of(bannerImage1, bannerImage2,
                bannerImage3, bannerImage4, bannerImage4));

        when(bannerRepo.getBannerByTypePerforating()).thenReturn(Optional.of(bannerDTO));
        when(bannerRepo.getImagesByBannerId(1L)).thenReturn(bannerDTO.getBannerImages());

        List<String> listImagesFileNameById = bannerService.getListImagesFileNameByBannerType(BannerType.PERFORATING);
        List<String> list = bannerDTO.getBannerImages().stream().map(BannerImage::getImage).toList();

        Assertions.assertFalse(listImagesFileNameById.isEmpty());
        Assertions.assertTrue(list.containsAll(listImagesFileNameById));

    }

    @Test
    void getListImagesFileNameByBannerType_PROMOTION() {
        BannerDTO bannerDTO = new BannerDTO(
                1L,
                BannerType.PROMOTION,
                4,
                "RED",
                true);
        BannerImage bannerImage1 = new BannerImage();
        bannerImage1.setImage("ImageName1");
        BannerImage bannerImage2 = new BannerImage();
        bannerImage2.setImage("ImageName2");
        BannerImage bannerImage3 = new BannerImage();
        bannerImage3.setImage("ImageName3");
        BannerImage bannerImage4 = new BannerImage();
        bannerImage4.setImage("ImageName4");
        BannerImage bannerImage5 = new BannerImage();
        bannerImage5.setImage("ImageName5");
        bannerDTO.setBannerImages(List.of(bannerImage1, bannerImage2,
                bannerImage3, bannerImage4, bannerImage4));

        when(bannerRepo.getBannerByTypePromotion()).thenReturn(Optional.of(bannerDTO));
        when(bannerRepo.getImagesByBannerId(1L)).thenReturn(bannerDTO.getBannerImages());

        List<String> listImagesFileNameById = bannerService.getListImagesFileNameByBannerType(BannerType.PROMOTION);
        List<String> list = bannerDTO.getBannerImages().stream().map(BannerImage::getImage).toList();

        Assertions.assertFalse(listImagesFileNameById.isEmpty());
        Assertions.assertTrue(list.containsAll(listImagesFileNameById));

    }

    @Test
    void updateImagesOnModel() {
        BannerDTO bannerDTO = new BannerDTO(
                1L,
                BannerType.PROMOTION,
                4,
                "RED",
                true);
        BannerImage bannerImage1 = new BannerImage();
        bannerImage1.setImage("ImageName1");
        BannerImage bannerImage2 = new BannerImage();
        bannerImage2.setImage("ImageName2");
        BannerImage bannerImage3 = new BannerImage();
        bannerImage3.setImage("ImageName3");
        BannerImage bannerImage4 = new BannerImage();
        bannerImage4.setImage("ImageName4");
        BannerImage bannerImage5 = new BannerImage();
        bannerImage5.setImage("ImageName5");
        bannerDTO.setBannerImages(List.of(bannerImage1, bannerImage2,
                bannerImage3, bannerImage4, bannerImage4));

        List<String> fileNamesFromDB = List.of("file1", "file2", "file3", "file4", "file5");

        BannerDTO bannerDTO1 = bannerService.updateImagesOnModel(bannerDTO, fileNamesFromDB);

        List<String> list = bannerDTO1.getBannerImages().stream().map(BannerImage::getImage).toList();

        Assertions.assertFalse(list.isEmpty());
        Assertions.assertTrue(fileNamesFromDB.containsAll(list));
    }
}