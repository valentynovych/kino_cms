package com.kino_cms.service;

import com.kino_cms.dto.FeedDTO;
import com.kino_cms.entity.FeedPage;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.FeedPageRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class FeedPageServiceTest {

    @Mock
    FeedPageRepo feedPageRepo;
    FeedPageService feedPageService;

    private FeedPage feedPage;
    private FeedPage translationPage;
    private FeedDTO feedDTO;

    @BeforeEach
    void setUp() {
        feedPageService = new FeedPageService(feedPageRepo);
        feedPage = new FeedPage();
        feedPage.setId(1L);
        feedPage.setFeedType(FeedType.FEED);
        feedPage.setLanguage(Language.UKRAINIAN);
        feedPage.setMainImage("mainImage");
        feedPage.setImage1("image1");
        feedPage.setImage2("image2");
        feedPage.setImage3("image3");
        feedPage.setImage4("image4");
        feedPage.setImage5("image5");
        feedPage.setDescription("description");
        feedPage.setTags("tag1,tag2,tag3");
        feedPage.setIsActivate(Boolean.TRUE);
        feedPage.setTranslatePageId(2L);
        feedPage.setPublishDate(new Date(LocalDateTime.now().getLong(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH)));
        SeoBlock seoBlock = new SeoBlock();
        seoBlock.setSeoTitle("seoTitle");
        seoBlock.setSeoKeywords("tag1, tag2, tag3");
        feedPage.setSeoBlock(seoBlock);


        feedDTO = new FeedDTO(feedPage.getId(), feedPage.getFeedType(), feedPage.getTitle(), feedPage.getDescription(),
                feedPage.getPublishDate(), feedPage.getMainImage(), feedPage.getImage1(), feedPage.getImage2(),
                feedPage.getImage3(), feedPage.getImage4(), feedPage.getImage5(), feedPage.getUrlVideo(),
                feedPage.getIsActivate(), feedPage.getLanguage(), feedPage.getCreateTime(), feedPage.getSeoBlock().getId(),
                feedPage.getSeoBlock().getSeoUrl(), feedPage.getSeoBlock().getSeoTitle(), feedPage.getSeoBlock().getSeoKeywords(),
                feedPage.getSeoBlock().getSeoDescription());

    }

    @Test
    void getFeedPageById() {
        when(feedPageRepo.findById(1L)).thenReturn(Optional.of(feedPage));
        Optional<FeedPage> feedPageById = feedPageService.getFeedPageById(1L);
        assertTrue(feedPageById.isPresent());
        FeedPage feedPage1 = feedPageById.get();
        assertEquals(feedPage.getFeedType(), feedPage1.getFeedType());
        assertEquals(feedPage.getTranslatePageId(), feedPage1.getTranslatePageId());
        assertEquals(feedPage.getIsActivate(), feedPage1.getIsActivate());
        assertEquals(feedPage.getImage1(), feedPage1.getImage1());
    }

    @Test
    void saveFeedPage() {
        translationPage = new FeedPage();
        translationPage.setId(feedPage.getTranslatePageId());
        translationPage.setTranslatePageId(feedPage.getId());

        when(feedPageRepo.save(feedPage)).thenReturn(feedPage);
        when(feedPageRepo.findById(feedPage.getTranslatePageId())).thenReturn(Optional.of(translationPage));
        when(feedPageRepo.save(translationPage)).thenReturn(translationPage);

        feedPageService.saveFeedPage(feedPage);
        feedPage.setTranslatePageId(null);
        feedPageService.saveFeedPage(feedPage);
    }

    @Test
    void deleteFeedPage() {
        feedPageService.deleteFeedPage(feedPage);
    }

    @Test
    void findFeedPagesByFeedType() {
        when(feedPageRepo.findFeedPagesByFeedType(FeedType.FEED)).thenReturn(List.of(feedPage));

        List<FeedPage> feedPagesByFeedType = feedPageService.findFeedPagesByFeedType(FeedType.FEED);
        assertFalse(feedPagesByFeedType.isEmpty());
        assertEquals(feedPagesByFeedType.get(0).getFeedType(), feedPage.getFeedType());

        feedPage.setFeedType(FeedType.PROMOTION);

        when(feedPageRepo.findFeedPagesByFeedType(FeedType.PROMOTION)).thenReturn(List.of(feedPage));
        List<FeedPage> feedPagesByFeedType1 = feedPageService.findFeedPagesByFeedType(FeedType.PROMOTION);
        assertFalse(feedPagesByFeedType1.isEmpty());
        assertEquals(feedPagesByFeedType1.get(0).getFeedType(), feedPage.getFeedType());
    }

    @Test
    void getListImagesFileNameById() {
        when(feedPageRepo.findById(feedPage.getId())).thenReturn(Optional.of(feedPage));
        List<String> fileNameFromFeedPage = List.of(feedPage.getMainImage(), feedPage.getImage1(),
                feedPage.getImage2(), feedPage.getImage3(), feedPage.getImage4(), feedPage.getImage5());
        List<String> listImagesFileNameById = feedPageService.getListImagesFileNameById(feedPage.getId());
        assertFalse(listImagesFileNameById.isEmpty());
        assertTrue(fileNameFromFeedPage.containsAll(listImagesFileNameById));
    }

    @Test
    void updateImagesOnModel() {
        List<String> fileNamesFromDB = List.of("file1", "file2", "file3", "file4", "file5", "fail6");
        FeedPage feedPage1 = feedPageService.updateImagesOnModel(feedPage, fileNamesFromDB);
        assertEquals(fileNamesFromDB.get(0), feedPage1.getMainImage());
        assertEquals(fileNamesFromDB.get(1), feedPage1.getImage1());
        assertEquals(fileNamesFromDB.get(2), feedPage1.getImage2());
        assertEquals(fileNamesFromDB.get(3), feedPage1.getImage3());
        assertEquals(fileNamesFromDB.get(4), feedPage1.getImage4());
        assertEquals(fileNamesFromDB.get(5), feedPage1.getImage5());
    }

    @Test
    void getAllFeedDTOByFeedTypeFeed() {
        when(feedPageRepo.getAllFeedDTOByTypeFeed(Language.UKRAINIAN)).thenReturn(List.of(feedDTO));
        when(feedPageRepo.getAllFeedDTOByTypeFeed(Language.ENGLISH)).thenReturn(List.of(feedDTO));

        LocaleContextHolder.setLocale(new Locale("uk"));
        List<FeedDTO> feedDTOList = feedPageService.getAllFeedDTOByFeedTypeFeed();
        assertFalse(feedDTOList.isEmpty());
        assertEquals(feedDTOList.get(0).getLanguage(), Language.UKRAINIAN);

        feedDTO.setLanguage(Language.ENGLISH);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        List<FeedDTO> feedDTOList1 = feedPageService.getAllFeedDTOByFeedTypeFeed();
        assertFalse(feedDTOList1.isEmpty());
        assertEquals(feedDTOList1.get(0).getLanguage(), Language.ENGLISH);
    }

    @Test
    void getAllFeedDTOByFeedTypePromotion() {
        when(feedPageRepo.getAllFeedDTOByTypePromotion(Language.UKRAINIAN)).thenReturn(List.of(feedDTO));
        when(feedPageRepo.getAllFeedDTOByTypePromotion(Language.ENGLISH)).thenReturn(List.of(feedDTO));

        LocaleContextHolder.setLocale(new Locale("uk"));
        List<FeedDTO> feedDTOList = feedPageService.getAllFeedDTOByFeedTypePromotion();
        assertFalse(feedDTOList.isEmpty());
        assertEquals(feedDTOList.get(0).getLanguage(), Language.UKRAINIAN);

        feedDTO.setLanguage(Language.ENGLISH);
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        List<FeedDTO> feedDTOList1 = feedPageService.getAllFeedDTOByFeedTypePromotion();
        assertFalse(feedDTOList1.isEmpty());
        assertEquals(feedDTOList1.get(0).getLanguage(), Language.ENGLISH);
    }

    @Test
    void getFeedDTOById() {
        when(feedPageRepo.getFeedDTOById(feedPage.getId())).thenReturn(feedDTO);
        FeedDTO feedDTOById = feedPageService.getFeedDTOById(feedPage.getId());
        assertNotNull(feedDTOById);
        assertEquals(feedPage.getFeedType(), feedDTOById.getFeedType());
        assertEquals(feedPage.getImage3(), feedDTOById.getImage3());
    }

    @Test
    void getFeedPageByTranslatePageId() {
        translationPage = new FeedPage();
        translationPage.setId(2L);
        translationPage.setTranslatePageId(1L);
        feedPage.setTranslatePageId(2L);
        feedPage.setTranslatePageId(translationPage.getId());
        when(feedPageRepo.findByTranslatePageId(feedPage.getTranslatePageId()))
                .thenReturn(Optional.of(translationPage));
        Optional<FeedPage> feedPageByTranslatePageId =
                feedPageService.getFeedPageByTranslatePageId(feedPage.getTranslatePageId());
        assertTrue(feedPageByTranslatePageId.isPresent());
    }
}