package com.kino_cms.service;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.dto.HallDTO;
import com.kino_cms.entity.Cinema;
import com.kino_cms.entity.Hall;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.HallRepo;
import com.kino_cms.utils.SaveUploadFileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HallServiceTest {
    @Mock
    HallRepo hallRepo;
    @Mock
    SaveUploadFileUtils uploadFileUtils;
    @Mock
    CinemaService cinemaService;
    HallService hallService;
    private Hall hall;
    private Hall translationHall;
    private HallDTO hallDTO;
    private Cinema cinema;

    @BeforeEach
    void setUp() {
        hallService = new HallService(hallRepo, cinemaService, uploadFileUtils);
        translationHall = new Hall();
        translationHall.setTranslatePageId(1L);
        translationHall.setId(2L);

        hall = new Hall();
        hall.setId(1L);
        hall.setLanguage(Language.UKRAINIAN);
        hall.setHallSchema("hallSchema");
        hall.setFirstBanner("firstBanner");
        hall.setName("cinemaName");
        hall.setImage1("image1");
        hall.setImage2("image2");
        hall.setImage3("image3");
        hall.setImage4("image4");
        hall.setImage5("image5");
        hall.setDescriptions("description");
        hall.setTranslatePageId(translationHall.getId());
        hall.setName("hallName");

        cinema = new Cinema();
        cinema.setId(1L);
        hall.setCinema(cinema);

        SeoBlock seoBlock = new SeoBlock();
        seoBlock.setSeoTitle("seoTitle");
        hall.setSeoBlock(seoBlock);

        hallDTO = new HallDTO(hall.getId(), hall.getName(), hall.getDescriptions(), hall.getHallSchema(),
                hall.getFirstBanner(), hall.getImage1(), hall.getImage2(), hall.getImage3(), hall.getImage4(),
                hall.getImage5(), hall.getLanguage(), hall.getCreateTime(), hall.getCinema().getId(),
                hall.getSeoBlock().getSeoUrl(), hall.getSeoBlock().getSeoTitle(), hall.getSeoBlock().getSeoKeywords(),
                hall.getSeoBlock().getSeoDescription(), hall.getTranslatePageId());
    }

    @Test
    void getHallById() {
        when(hallRepo.findById(hall.getId())).thenReturn(Optional.of(hall));
        Optional<Hall> hallById = hallService.getHallById(hall.getId());
        assertTrue(hallById.isPresent());

        when(hallRepo.findById(2L)).thenReturn(Optional.empty());
        Optional<Hall> hallById1 = hallService.getHallById(2L);
        assertTrue(hallById1.isEmpty());
    }

    @Test
    void saveHall() {
        when(cinemaService.getCinemaById(hall.getCinema().getId())).thenReturn(Optional.of(cinema));
        cinema.setTranslatePageId(2L);
        cinema.setLanguage(Language.UKRAINIAN);
        Cinema cinemaTranslate = new Cinema();
        cinemaTranslate.setId(2L);
        cinemaTranslate.setTranslatePageId(1L);
        cinemaTranslate.setLanguage(Language.ENGLISH);

        when(hallRepo.findById(hall.getTranslatePageId())).thenReturn(Optional.of(translationHall));
        when(hallRepo.save(hall)).thenReturn(hall);

        hallService.saveHall(hall);

        cinema.setLanguage(Language.ENGLISH);
        when(cinemaService.getCinemaById(cinema.getTranslatePageId())).thenReturn(Optional.of(cinemaTranslate));
        hallService.saveHall(hall);

        hall.setTranslatePageId(null);
        hallService.saveHall(hall);
    }

    @Test
    void deleteHall() {
        when(hallRepo.findById(1L)).thenReturn(Optional.of(hall));
        hallService.deleteHall(1L);

    }

    @Test
    void getHallDtoById() {
        when(hallRepo.getHallDtoById(1L)).thenReturn(Optional.of(hallDTO));
        Optional<HallDTO> hallDtoById = hallService.getHallDtoById(1L);
        assertTrue(hallDtoById.isPresent());
        HallDTO hallDTO1 = hallDtoById.get();
        assertEquals(hallDTO.getHallSchema(), hallDTO1.getHallSchema());
        assertEquals(hallDTO.getLanguage(), hallDTO1.getLanguage());
        assertEquals(hallDTO.getId(), hallDTO1.getId());
    }

    @Test
    void getAllHallByCinema() {
        when(hallRepo.getAllHallByCinema(any(Cinema.class)))
                .thenReturn(List.of(hallDTO, new HallDTO(), new HallDTO()));
        CinemaDTO cinemaDTO = new CinemaDTO();
        cinemaDTO.setId(cinema.getId());
        List<HallDTO> allHallByCinema = hallService.getAllHallByCinema(cinemaDTO);
        assertFalse(allHallByCinema.isEmpty());
        assertEquals(hallDTO, allHallByCinema.get(0));
        assertEquals(3, allHallByCinema.size());

    }

    @Test
    void getListImagesFileNameById() {
        when(hallRepo.findById(hall.getId())).thenReturn(Optional.of(hall));
        List<String> filenameFromHallDto = List.of(hall.getHallSchema(), hall.getFirstBanner(),
                hall.getImage1(), hall.getImage2(), hall.getImage3(), hall.getImage4(), hall.getImage5());
        List<String> listImagesFileNameById = hallService.getListImagesFileNameById(hall.getId());
        assertFalse(listImagesFileNameById.isEmpty());
        assertTrue(filenameFromHallDto.containsAll(listImagesFileNameById));
    }

    @Test
    void updateImagesOnModel() {
        List<String> fileNamesFromDB = List.of("file1", "file2", "file3", "file4", "file5", "fail6", "file7");
        HallDTO hallDTO1 = hallService.updateImagesOnModel(hallDTO, fileNamesFromDB);
        assertEquals(fileNamesFromDB.get(0), hallDTO1.getHallSchema());
        assertEquals(fileNamesFromDB.get(1), hallDTO1.getFirstBanner());
        assertEquals(fileNamesFromDB.get(2), hallDTO1.getImage1());
        assertEquals(fileNamesFromDB.get(3), hallDTO1.getImage2());
        assertEquals(fileNamesFromDB.get(4), hallDTO1.getImage3());
        assertEquals(fileNamesFromDB.get(5), hallDTO1.getImage4());
        assertEquals(fileNamesFromDB.get(6), hallDTO1.getImage5());
    }

    @Test
    void getCinemaIdByHall() {
        when(hallRepo.findById(hall.getId())).thenReturn(Optional.of(hall));
        Long cinemaIdByHall = hallService.getCinemaIdByHall(hall.getId());
        assertNotNull(cinemaIdByHall);
        assertEquals(1L, cinemaIdByHall);

        when(hallRepo.findById(2L)).thenReturn(Optional.empty());
        Long cinemaIdByHall1 = hallService.getCinemaIdByHall(2L);
        assertNotNull(cinemaIdByHall1);
        assertEquals(0L, cinemaIdByHall1);
    }

    @Test
    void saveHallDTO() {
        when(hallRepo.findById(hallDTO.getId())).thenReturn(Optional.of(hall));
        when(hallRepo.save(any(Hall.class))).thenReturn(hall);
        when(hallRepo.findById(hall.getTranslatePageId())).thenReturn(Optional.of(hall));

        hallService.saveHallDTO(hallDTO);

        when(hallRepo.findById(2L)).thenReturn(Optional.empty());
        hallDTO.setId(2L);
        hallService.saveHallDTO(hallDTO);
    }

    @Test
    void getHallDtoByTranslatePageId() {
        when(hallRepo.findByTranslatePageId(1L)).thenReturn(Optional.of(hallDTO));
        Optional<HallDTO> hallDtoByTranslatePageId = hallService.getHallDtoByTranslatePageId(1L);
        assertTrue(hallDtoByTranslatePageId.isPresent());
    }
}