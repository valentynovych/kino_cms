package com.kino_cms.service;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import com.kino_cms.enums.PageType;
import com.kino_cms.repository.GeneralPageRepo;
import com.kino_cms.utils.SaveUploadFileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralPageServiceTest {
    @Mock
    GeneralPageRepo generalPageRepo;
    @Mock
    SaveUploadFileUtils uploadFileUtils;

    GeneralPageService generalPageService;

    private GeneralPage generalPage;
    private GeneralPage translateGeneralPage;
    private GeneralPageDTO generalPageDTO;


    @BeforeEach
    void setUp() {
        generalPageService = new GeneralPageService(generalPageRepo, uploadFileUtils);

        translateGeneralPage = new GeneralPage();
        translateGeneralPage.setId(2L);
        translateGeneralPage.setTranslatePageId(1L);
        translateGeneralPage.setLanguage(Language.ENGLISH);

        generalPage = new GeneralPage();
        generalPage.setId(1L);
        generalPage.setTranslatePageId(2L);
        generalPage.setPageType(PageType.OTHER_PAGE);
        generalPage.setLanguage(Language.UKRAINIAN);
        generalPage.setIsActive(Boolean.TRUE);
        generalPage.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
        generalPage.setDescription("description");
        generalPage.setMainImage("mainImage");
        generalPage.setImage1("image1");
        generalPage.setImage2("image2");
        generalPage.setImage3("image3");
        generalPage.setImage4("image4");
        generalPage.setImage5("image5");
        generalPage.setTitle("title");
        SeoBlock seoBlock = new SeoBlock();
        seoBlock.setSeoTitle("seoTitle");
        generalPage.setSeoBlock(seoBlock);

        generalPageDTO = new GeneralPageDTO(generalPage.getId(), generalPage.getPageType(), generalPage.getTitle(),
                generalPage.getDescription(), generalPage.getMainImage(), generalPage.getImage1(),
                generalPage.getImage2(), generalPage.getImage3(), generalPage.getImage4(), generalPage.getImage5(),
                generalPage.getLanguage(), generalPage.getIsActive(), generalPage.getCreateTime(),
                generalPage.getSeoBlock().getSeoUrl(), generalPage.getSeoBlock().getSeoTitle(),
                generalPage.getSeoBlock().getSeoKeywords(), generalPage.getSeoBlock().getSeoDescription(),
                generalPage.getTranslatePageId());

    }

    @Test
    void getGeneralPageDTOById() {
        when(generalPageRepo.getGeneralPageDTOById(generalPageDTO.getId()))
                .thenReturn(Optional.of(generalPageDTO));
        Optional<GeneralPageDTO> generalPageDTOById = generalPageService.getGeneralPageDTOById(generalPageDTO.getId());
        assertTrue(generalPageDTOById.isPresent());
        GeneralPageDTO generalPageDTO1 = generalPageDTOById.get();
        assertEquals(generalPageDTO.getId(), generalPageDTO1.getId());
        assertEquals(generalPageDTO.getPageType(), generalPageDTO1.getPageType());
        assertEquals(generalPageDTO.getLanguage(), generalPageDTO1.getLanguage());
    }

    @Test
    void findById() {
        when(generalPageRepo.findById(generalPage.getId())).thenReturn(Optional.of(generalPage));
        Optional<GeneralPage> byId = generalPageService.findById(generalPage.getId());
        assertTrue(byId.isPresent());
        GeneralPage generalPage1 = byId.get();
        assertEquals(generalPage.getMainImage(), generalPage1.getMainImage());
        assertEquals(generalPage.getCreateTime(), generalPage1.getCreateTime());
        assertEquals(generalPage.getTitle(), generalPage1.getTitle());
    }

    @Test
    void saveGeneralPageDTO() {
        when(generalPageRepo.findById(generalPage.getId())).thenReturn(Optional.of(generalPage));
        when(generalPageRepo.save(any(GeneralPage.class))).thenReturn(generalPage);
        when(generalPageRepo.findById(generalPage.getTranslatePageId())).thenReturn(Optional.of(generalPage));

        generalPageService.saveGeneralPageDTO(generalPageDTO);

        when(generalPageRepo.findById(2L)).thenReturn(Optional.empty());
        generalPageDTO.setId(2L);
        generalPageService.saveGeneralPageDTO(generalPageDTO);
    }

    @Test
    void delete() {
        generalPageService.delete(generalPage);
    }

    @Test
    void save() {
        generalPageService.save(generalPage);
    }

    @Test
    void getAllOtherPages() {
        when(generalPageRepo.getAllByPageTypeOtherPage(Language.UKRAINIAN)).thenReturn(List.of(generalPageDTO));
        when(generalPageRepo.getAllByPageTypeOtherPage(Language.ENGLISH)).thenReturn(List.of(generalPageDTO));

        LocaleContextHolder.setLocale(new Locale("uk"));
        List<GeneralPageDTO> allOtherPages = generalPageService.getAllOtherPages();
        assertFalse(allOtherPages.isEmpty());
        assertEquals(allOtherPages.get(0).getLanguage(), Language.UKRAINIAN);

        generalPageDTO.setLanguage(Language.ENGLISH);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        List<GeneralPageDTO> allOtherPages1 = generalPageService.getAllOtherPages();
        assertFalse(allOtherPages1.isEmpty());
        assertEquals(allOtherPages1.get(0).getLanguage(), Language.ENGLISH);
        assertEquals(allOtherPages1.get(0).getPageType(), PageType.OTHER_PAGE);
    }

    @Test
    void getGeneralPageDTOAboutCinema() {
        generalPageDTO.setPageType(PageType.ABOUT_CINEMA);
        when(generalPageRepo.getAboutCinemaPageUk(Language.UKRAINIAN)).thenReturn(generalPageDTO);
        when(generalPageRepo.getAboutCinemaPageUk(Language.ENGLISH)).thenReturn(generalPageDTO);

        LocaleContextHolder.setLocale(new Locale("uk"));
        GeneralPageDTO dtoAboutCinema = generalPageService.getGeneralPageDTOAboutCinema();
        assertNotNull(dtoAboutCinema);
        assertEquals(dtoAboutCinema.getLanguage(), Language.UKRAINIAN);
        assertEquals(dtoAboutCinema.getPageType(), PageType.ABOUT_CINEMA);

        generalPageDTO.setLanguage(Language.ENGLISH);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        GeneralPageDTO dtoAboutCinema1 = generalPageService.getGeneralPageDTOAboutCinema();
        assertNotNull(dtoAboutCinema1);
        assertEquals(dtoAboutCinema1.getLanguage(), Language.ENGLISH);
        assertEquals(dtoAboutCinema1.getPageType(), PageType.ABOUT_CINEMA);
    }

    @Test
    void getAllPageByPageTypeForMenu() {
        generalPageDTO.setPageType(PageType.ABOUT_CINEMA);
        when(generalPageRepo.getAllUkPageByPageTypeUnion(Language.UKRAINIAN)).thenReturn(List.of(generalPage));
        when(generalPageRepo.getAllUkPageByPageTypeUnion(Language.ENGLISH)).thenReturn(List.of(generalPage));

        Locale locale = new Locale("uk");
        List<GeneralPage> dtoByLanguagePageId = generalPageService.getAllPageByPageTypeForMenu(locale);
        assertFalse(dtoByLanguagePageId.isEmpty());
        GeneralPage generalPage1 = dtoByLanguagePageId.get(0);
        assertEquals(generalPage1.getLanguage(), Language.UKRAINIAN);

        generalPage.setLanguage(Language.ENGLISH);
        locale = Locale.ENGLISH;
        List<GeneralPage> dtoByLanguagePageId1 = generalPageService.getAllPageByPageTypeForMenu(locale);
        assertFalse(dtoByLanguagePageId.isEmpty());
        GeneralPage generalPage2 = dtoByLanguagePageId1.get(0);
        assertEquals(generalPage2.getLanguage(), Language.ENGLISH);
    }

    @Test
    void getGeneralPageDTOByLanguagePageId() {
        when(generalPageRepo.getGeneralPageDTOByLanguagePageId(1L)).thenReturn(Optional.of(generalPageDTO));
        Optional<GeneralPageDTO> hallDtoByTranslatePageId = generalPageService.getGeneralPageDTOByLanguagePageId(1L);
        assertTrue(hallDtoByTranslatePageId.isPresent());
    }

    @Test
    void getListImagesFileNameById() {
        when(generalPageRepo.findById(generalPage.getId())).thenReturn(Optional.of(generalPage));
        List<String> filenameFromGeneralPage = List.of(generalPage.getMainImage(), generalPage.getImage1(),
                generalPage.getImage2(), generalPage.getImage3(), generalPage.getImage4(), generalPage.getImage5());
        List<String> listImagesFileNameById = generalPageService.getListImagesFileNameById(generalPage.getId());
        assertFalse(listImagesFileNameById.isEmpty());
        assertTrue(filenameFromGeneralPage.containsAll(listImagesFileNameById));
    }
}