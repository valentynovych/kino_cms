package com.kino_cms.service;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.entity.Cinema;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.CinemaRepo;
import com.kino_cms.utils.SaveUploadFileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CinemaServiceTest {

    @Mock
    CinemaRepo cinemaRepo;
    @Mock
    SaveUploadFileUtils uploadService;
    CinemaService cinemaService;
    private Cinema cinema;
    private CinemaDTO cinemaDTO;


    @BeforeEach
    void setUp() {
        cinemaService = new CinemaService(cinemaRepo, uploadService);

        cinema = new Cinema();
        cinema.setId(0L);
        cinema.setLanguage(Language.UKRAINIAN);
        cinema.setLogoImage("logoImage");
        cinema.setFirstBanner("firstBanner");
        cinema.setName("cinemaName");
        cinema.setConditions("cinemaConditions");
        cinema.setDescription("cinemaDescription");
        cinema.setImage1("image1");
        cinema.setImage2("image2");
        cinema.setImage3("image3");
        cinema.setImage4("image4");
        cinema.setImage5("image5");
        SeoBlock seoBlock = new SeoBlock();
        seoBlock.setSeoTitle("seoTitle");
        cinema.setSeoBlock(seoBlock);

        cinemaDTO = new CinemaDTO(cinema.getId(), cinema.getName(), cinema.getDescription(),
                cinema.getConditions(), cinema.getLogoImage(), cinema.getFirstBanner(), cinema.getImage1(),
                cinema.getImage2(), cinema.getImage3(), cinema.getImage4(), cinema.getImage5(), cinema.getLanguage(),
                "seoUrl", "seoTitle", "seoKeywords", "seoDescription", 1L);
    }

    @Test
    void getCinemaById() {
        when(cinemaRepo.findById(0L)).thenReturn(Optional.of(cinema));
        Optional<Cinema> cinemaById = cinemaService.getCinemaById(0L);
        assertTrue(cinemaById.isPresent());
    }

    @Test
    void getAllCinemaDto() {
        when(cinemaRepo.getAllCinemaDto(Language.UKRAINIAN)).thenReturn(List.of(cinemaDTO));
        when(cinemaRepo.getAllCinemaDto(Language.ENGLISH)).thenReturn(List.of(cinemaDTO));

        LocaleContextHolder.setLocale(new Locale("uk"));
        List<CinemaDTO> allCinemaDto = cinemaService.getAllCinemaDto();
        assertFalse(allCinemaDto.isEmpty());
        assertEquals(allCinemaDto.get(0).getLanguage(), Language.UKRAINIAN);

        cinemaDTO.setLanguage(Language.ENGLISH);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        List<CinemaDTO> allCinemaDto1 = cinemaService.getAllCinemaDto();
        assertFalse(allCinemaDto1.isEmpty());
        assertEquals(allCinemaDto1.get(0).getLanguage(), Language.ENGLISH);

    }

    @Test
    void deleteCinemaById() {
        when(cinemaRepo.findById(0L)).thenReturn(Optional.of(cinema));
        cinemaService.deleteCinemaById(0L);

        when(cinemaRepo.findById(1L)).thenReturn(Optional.empty());
        cinemaService.deleteCinemaById(1L);
    }

    @Test
    void saveCinema() {
        when(cinemaRepo.save(ArgumentMatchers.any(Cinema.class))).thenReturn(cinema);
        cinema.setTranslatePageId(null);
        Cinema cinema1 = cinemaService.saveCinema(cinema);
        assertEquals(cinema.getLanguage(), cinema1.getLanguage());
        assertEquals(cinema.getName(), cinema1.getName());

        cinema.setTranslatePageId(0L);
        when(cinemaRepo.findById(0L)).thenReturn(Optional.of(cinema));
        Cinema cinema2 = cinemaService.saveCinema(cinema);
        assertEquals(cinema.getId(), cinema2.getTranslatePageId());
        assertEquals(cinema.getLanguage(), cinema2.getLanguage());

    }

    @Test
    void getCinemaDtoById() {
        when(cinemaRepo.getCinemaDtoById(0L)).thenReturn(Optional.of(cinemaDTO));
        Optional<CinemaDTO> cinemaDtoById = cinemaService.getCinemaDtoById(0L);
        assertTrue(cinemaDtoById.isPresent());
        CinemaDTO cinemaDTO1 = cinemaDtoById.get();

        assertEquals(cinemaDTO.getLanguage(), cinemaDTO1.getLanguage());
        assertEquals(cinemaDTO.getImage2(), cinemaDTO1.getImage2());
        assertEquals(cinemaDTO.getConditions(), cinemaDTO1.getConditions());
    }

    @Test
    void getPresentCinemaDtoById() {
        when(cinemaRepo.getCinemaDtoById(0L)).thenReturn(Optional.of(cinemaDTO));
        CinemaDTO cinemaDtoById = cinemaService.getPresentCinemaDtoById(0L);

        assertEquals(cinemaDTO.getLanguage(), cinemaDtoById.getLanguage());
        assertEquals(cinemaDTO.getImage2(), cinemaDtoById.getImage2());
        assertEquals(cinemaDTO.getConditions(), cinemaDtoById.getConditions());

        when(cinemaRepo.getCinemaDtoById(1L)).thenReturn(Optional.empty());
        CinemaDTO cinemaDTO1 = cinemaService.getPresentCinemaDtoById(1L);

        assertNull(cinemaDTO1.getConditions());
        assertNull(cinemaDTO1.getId());
        assertNull(cinemaDTO1.getName());
    }

    @Test
    void saveCinemaDto() {
        when(cinemaRepo.findById(0L)).thenReturn(Optional.of(cinema));
        when(cinemaRepo.save(ArgumentMatchers.any(Cinema.class))).thenReturn(cinema);
        cinema.setTranslatePageId(null);
        cinemaDTO.setTranslatePageId(null);

        Cinema cinema1 = cinemaService.saveCinemaDto(cinemaDTO);
        assertEquals(cinema.getName(), cinema1.getName());
        assertEquals(cinema.getImage3(), cinema1.getImage3());
        assertEquals(cinema.getDescription(), cinema1.getDescription());

        when(cinemaRepo.findById(1L)).thenReturn(Optional.empty());
        cinemaDTO.setId(1L);

        Cinema cinema2 = cinemaService.saveCinemaDto(cinemaDTO);
        assertEquals(cinema.getName(), cinema2.getName());
        assertEquals(cinema.getImage3(), cinema2.getImage3());
        assertEquals(cinema.getDescription(), cinema2.getDescription());

    }

    @Test
    void getListImagesFileNameById() {
        when(cinemaRepo.findById(cinema.getId())).thenReturn(Optional.of(cinema));
        List<String> filenameFromCinemaDto = List.of(cinema.getLogoImage(), cinema.getFirstBanner(),
                cinema.getImage1(), cinema.getImage2(), cinema.getImage3(), cinema.getImage4(), cinema.getImage5());
        List<String> listImagesFileNameById = cinemaService.getListImagesFileNameById(cinema.getId());
        assertFalse(listImagesFileNameById.isEmpty());
        assertTrue(filenameFromCinemaDto.containsAll(listImagesFileNameById));
    }

    @Test
    void updateImagesOnModel() {
        List<String> fileNamesFromDB = List.of("file1", "file2", "file3", "file4", "file5", "fail6", "file7");
        CinemaDTO cinemaDTO1 = cinemaService.updateImagesOnModel(cinemaDTO, fileNamesFromDB);
        assertEquals(fileNamesFromDB.get(0), cinemaDTO1.getLogoImage());
        assertEquals(fileNamesFromDB.get(1), cinemaDTO1.getFirstBanner());
        assertEquals(fileNamesFromDB.get(2), cinemaDTO1.getImage1());
        assertEquals(fileNamesFromDB.get(3), cinemaDTO1.getImage2());
        assertEquals(fileNamesFromDB.get(4), cinemaDTO1.getImage3());
        assertEquals(fileNamesFromDB.get(5), cinemaDTO1.getImage4());
        assertEquals(fileNamesFromDB.get(6), cinemaDTO1.getImage5());
    }

    @Test
    void getCinemaByTranslatePageId() {
        when(cinemaRepo.getCinemaDtoByTranslatePageId(0L)).thenReturn(Optional.of(cinemaDTO));
        Optional<CinemaDTO> cinemaByTranslatePageId = cinemaService.getCinemaByTranslatePageId(0L);
        assertTrue(cinemaByTranslatePageId.isPresent());
    }
}