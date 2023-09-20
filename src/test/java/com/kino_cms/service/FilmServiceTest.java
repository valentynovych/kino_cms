package com.kino_cms.service;

import com.kino_cms.dto.FilmDTO;
import com.kino_cms.entity.Film;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.FilmType;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.FilmRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class FilmServiceTest {

    @Mock
    FilmRepo filmRepo;

    FilmService filmService;
    private Film film;
    private Film translateFilmPage;
    private FilmDTO filmDTO;

    @BeforeEach
    void setUp() {
        filmService = new FilmService(filmRepo);
        film = new Film();
        film.setId(1L);
        film.setLanguage(Language.UKRAINIAN);
        film.setMainImage("mainImage");
        film.setName("filmName");
        film.setDescription("filmDescription");
        film.setImage1("image1");
        film.setImage2("image2");
        film.setImage3("image3");
        film.setImage4("image4");
        film.setImage5("image5");
        film.setFilmTypeList(List.of(FilmType.D2, FilmType.D3, FilmType.IMAX));
        film.setDateOfPremiere(new Date(LocalDateTime.now().getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)));
        film.setDateEndPremiere(new Date(LocalDateTime.now().plusDays(1).getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)));
        SeoBlock seoBlock = new SeoBlock();
        seoBlock.setSeoTitle("seoTitle");
        film.setSeoBlock(seoBlock);

        translateFilmPage = new Film();
        translateFilmPage.setId(2L);
        translateFilmPage.setTranslatePageId(film.getId());
        film.setTranslatePageId(translateFilmPage.getId());

        filmDTO = new FilmDTO(film.getId(), film.getName(), film.getDescription(), film.getMainImage(),
                film.getImage1(), film.getImage2(), film.getImage3(), film.getImage4(), film.getImage5(),
                film.getUrlVideo(), film.getLanguage(), film.getSeoBlock().getSeoUrl(), film.getSeoBlock().getSeoTitle(),
                film.getSeoBlock().getSeoKeywords(), film.getSeoBlock().getSeoDescription(), film.getDateOfPremiere(),
                film.getDateEndPremiere(), film.getDatePremiereFromTo(), film.getTranslatePageId());

    }

    @Test
    void saveFilm() {
        when(filmRepo.save(film)).thenReturn(film);
        when(filmRepo.findById(film.getTranslatePageId())).thenReturn(Optional.of(translateFilmPage));
        when(filmRepo.save(translateFilmPage)).thenReturn(translateFilmPage);

        filmService.saveFilm(film);
        film.setTranslatePageId(null);
        filmService.saveFilm(film);
    }

    @Test
    void getFilmById() {
        when(filmRepo.findById(1L)).thenReturn(Optional.of(film));
        Optional<Film> filmById = filmService.getFilmById(1L);
        assertTrue(filmById.isPresent());
    }

    @Test
    void getFilmDtoById() {
        when(filmRepo.getFilmDtoById(1L)).thenReturn(Optional.of(filmDTO));
        Optional<FilmDTO> filmById = filmService.getFilmDtoById(1L);
        assertTrue(filmById.isPresent());
    }

    @Test
    void getAllFilmIsReleasedNow() {
        when(filmRepo.getAllFilmIsReleasedNow(Language.UKRAINIAN)).thenReturn(List.of(filmDTO));
        when(filmRepo.getAllFilmIsReleasedNow(Language.ENGLISH)).thenReturn(List.of(filmDTO));

        LocaleContextHolder.setLocale(new Locale("uk"));
        List<FilmDTO> filmDTOList = filmService.getAllFilmIsReleasedNow();
        assertFalse(filmDTOList.isEmpty());
        assertEquals(filmDTOList.get(0).getLanguage(), Language.UKRAINIAN);

        filmDTO.setLanguage(Language.ENGLISH);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        List<FilmDTO> filmDTOList1 = filmService.getAllFilmIsReleasedNow();
        assertFalse(filmDTOList1.isEmpty());
        assertEquals(filmDTOList1.get(0).getLanguage(), Language.ENGLISH);
    }

    @Test
    void getAllFilmReleasedSoon() {
        when(filmRepo.getAllFilmReleasedSoon(Language.UKRAINIAN)).thenReturn(List.of(filmDTO));
        when(filmRepo.getAllFilmReleasedSoon(Language.ENGLISH)).thenReturn(List.of(filmDTO));

        LocaleContextHolder.setLocale(new Locale("uk"));
        List<FilmDTO> filmDTOList = filmService.getAllFilmReleasedSoon();
        assertFalse(filmDTOList.isEmpty());
        assertEquals(filmDTOList.get(0).getLanguage(), Language.UKRAINIAN);

        filmDTO.setLanguage(Language.ENGLISH);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        List<FilmDTO> filmDTOList1 = filmService.getAllFilmReleasedSoon();
        assertFalse(filmDTOList1.isEmpty());
        assertEquals(filmDTOList1.get(0).getLanguage(), Language.ENGLISH);
    }

    @Test
    void getFilmTypeListById() {
        when(filmRepo.findById(1L)).thenReturn(Optional.of(film));
        when(filmRepo.findById(2L)).thenReturn(Optional.empty());
        List<FilmType> filmTypeListById = filmService.getFilmTypeListById(1L);
        List<FilmType> filmTypeList = film.getFilmTypeList();
        assertFalse(filmTypeListById.isEmpty());
        assertTrue(filmTypeList.containsAll(filmTypeListById));

        List<FilmType> filmTypeListById1 = filmService.getFilmTypeListById(2L);
        assertTrue(filmTypeListById1.isEmpty());


    }

    @Test
    void getListImagesFileNameById() {
        when(filmRepo.findById(film.getId())).thenReturn(Optional.of(film));
        List<String> filenameFromFilm = List.of(film.getMainImage(), film.getImage1(),
                film.getImage2(), film.getImage3(), film.getImage4(), film.getImage5());
        List<String> listImagesFileNameById = filmService.getListImagesFileNameById(film.getId());
        assertFalse(listImagesFileNameById.isEmpty());
        assertTrue(filenameFromFilm.containsAll(listImagesFileNameById));
    }

    @Test
    void updateImagesOnModel() {
        List<String> fileNamesFromDB = List.of("file1", "file2", "file3", "file4", "file5", "fail6");
        FilmDTO filmDTO1 = filmService.updateImagesOnModel(filmDTO, fileNamesFromDB);
        assertEquals(fileNamesFromDB.get(0), filmDTO1.getMainImage());
        assertEquals(fileNamesFromDB.get(1), filmDTO1.getImage1());
        assertEquals(fileNamesFromDB.get(2), filmDTO1.getImage2());
        assertEquals(fileNamesFromDB.get(3), filmDTO1.getImage3());
        assertEquals(fileNamesFromDB.get(4), filmDTO1.getImage4());
        assertEquals(fileNamesFromDB.get(5), filmDTO1.getImage5());
    }

    @Test
    void saveFilmDTO() {
        when(filmRepo.findById(film.getId())).thenReturn(Optional.of(film));
        when(filmRepo.save(ArgumentMatchers.any(Film.class))).thenReturn(film);
        film.setTranslatePageId(null);
        filmDTO.setTranslatePageId(null);
        filmDTO.setDatePremiereFromTo("2023-09-02 to 2023-09-16");

        filmService.saveFilmDTO(filmDTO);

        when(filmRepo.findById(2L)).thenReturn(Optional.empty());
        filmDTO.setId(2L);

        filmService.saveFilmDTO(filmDTO);
    }

    @Test
    void getFilmDtoByTranslatePageId() {
        filmDTO.setId(film.getTranslatePageId());
        filmDTO.setTranslatePageId(film.getId());
        when(filmRepo.getFilmDtoByTranslatePageId(film.getTranslatePageId())).thenReturn(Optional.of(filmDTO));
        Optional<FilmDTO> cinemaByTranslatePageId = filmService.getFilmDtoByTranslatePageId(film.getTranslatePageId());
        assertTrue(cinemaByTranslatePageId.isPresent());
    }
}