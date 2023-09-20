package com.kino_cms.service;

import com.kino_cms.dto.FeedDTO;
import com.kino_cms.entity.FeedPage;
import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.FeedPageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class FeedPageService {

    private final FeedPageRepo feedPageRepo;

    public Optional<FeedPage> getFeedPageById(Long id) {
        log.info("-> start execution method getFeedPageById() on id: " + id);
        Optional<FeedPage> byId = feedPageRepo.findById(id);
        log.info("-> exit from method getFeedPageById(), return optional isPresent: " + byId.isPresent());
        return byId;
    }

    public void saveFeedPage(FeedPage feedPage) {
        log.info("-> start execution method saveFeedPage(FeedPage) " + feedPage.toString());
        if (feedPage.getTranslatePageId() != null) {
            FeedPage feedPage1 = feedPageRepo.save(feedPage);
            log.info("-> saveFeedPage > success saving");
            FeedPage toSetTranslate = feedPageRepo.findById(feedPage.getTranslatePageId()).get();
            toSetTranslate.setTranslatePageId(feedPage1.getId());
            log.info("-> update relation translate page in id:" + feedPage1.getId());
            feedPageRepo.save(toSetTranslate);
            log.info("-> save relation translate page with id:" + feedPage1.getTranslatePageId());
        } else {
            feedPageRepo.save(feedPage);
            log.info("-> saveFeedPage > success saving");
        }
    }

    public void deleteFeedPage(FeedPage feedPage) {
        log.info("-> start execution method deleteFeedPage() on FeedPageId: " + feedPage.getId());
        feedPageRepo.delete(feedPage);
        log.info("-> success deleting FeedPage with id: " + feedPage.getId());
    }

    public List<FeedPage> findFeedPagesByFeedType(FeedType feedType) {
        log.info("-> start execution method findFeedPagesByFeedType(FeedType) on FeedType: " + feedType);
        List<FeedPage> feedPagesByFeedType = feedPageRepo.findFeedPagesByFeedType(feedType);
        log.info("-> exit from method findFeedPagesByFeedType(FeedType) FeedPage list size: " + feedPagesByFeedType.size());
        return feedPagesByFeedType;
    }

    public List<String> getListImagesFileNameById(Long id) {
        log.info(String.format("-> start execution method getListImagesFileNameById(Id %s)", id));
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", ""));
        FeedPage feedPage;

        Optional<FeedPage> feedPageOptional = getFeedPageById(id);

        if (feedPageOptional.isPresent()) {
            feedPage = feedPageOptional.get();

            log.info("-> getListImagesFileNameById > feedPage isPresent, get image from feedPage");
            fileNamesFromDB.set(0, feedPage.getMainImage());
            fileNamesFromDB.set(1, feedPage.getImage1());
            fileNamesFromDB.set(2, feedPage.getImage2());
            fileNamesFromDB.set(3, feedPage.getImage3());
            fileNamesFromDB.set(4, feedPage.getImage4());
            fileNamesFromDB.set(5, feedPage.getImage5());
        }
        log.info("-> exit from method getListImagesFileNameById()");
        return fileNamesFromDB;
    }

    public FeedPage updateImagesOnModel(FeedPage feedPageModel, List<String> fileNamesFromDB) {
        log.info(String.format("-> start execution private method updateImagesOnModel(FeedPage %s)", feedPageModel.toString()));
        feedPageModel.setMainImage(fileNamesFromDB.get(0));
        feedPageModel.setImage1(fileNamesFromDB.get(1));
        feedPageModel.setImage2(fileNamesFromDB.get(2));
        feedPageModel.setImage3(fileNamesFromDB.get(3));
        feedPageModel.setImage4(fileNamesFromDB.get(4));
        feedPageModel.setImage5(fileNamesFromDB.get(5));
        log.info("-> success update image filename \n\t\t exit from method updateImagesOnModel()");
        return feedPageModel;
    }

    public List<FeedDTO> getAllFeedDTOByFeedTypeFeed() {
        log.info("-> start execution method getAllFeedDTOByFeedTypeFeed()");
        Locale locale = LocaleContextHolder.getLocale();
        List<FeedDTO> allFeedDTO;
        log.info("-> start execution method getAllFeedDTOByFeedTypeFeed on locale: " + locale);
        if (locale.getLanguage().equals("en")) {
            allFeedDTO = feedPageRepo.getAllFeedDTOByTypeFeed(Language.ENGLISH);
        } else {
            allFeedDTO = feedPageRepo.getAllFeedDTOByTypeFeed(Language.UKRAINIAN);
        }
        log.info("-> exit from method getAllFeedDTOByFeedTypeFeed(), return FeedPageList size: " + allFeedDTO.size());
        return allFeedDTO;
    }

    public List<FeedDTO> getAllFeedDTOByFeedTypePromotion() {
        log.info("-> start execution method getAllFeedDTOByFeedTypePromotion()");
        Locale locale = LocaleContextHolder.getLocale();
        List<FeedDTO> allFeedDTO;
        log.info("-> start execution method getAllFeedDTOByFeedTypeFeed on locale: " + locale);
        if (locale.getLanguage().equals("en")) {
            allFeedDTO = feedPageRepo.getAllFeedDTOByTypePromotion(Language.ENGLISH);
        } else {
            allFeedDTO = feedPageRepo.getAllFeedDTOByTypePromotion(Language.UKRAINIAN);
        }
        log.info("-> exit from method getAllFeedDTOByFeedTypePromotion(), return FeedPageList size: " + allFeedDTO.size());
        return allFeedDTO;
    }

    public FeedDTO getFeedDTOById(Long feedId) {
        log.info("-> start execution method getFeedDTOById() by id: " + feedId);
        FeedDTO feedDTO = feedPageRepo.getFeedDTOById(feedId);
        log.info("-> exit from method getFeedDTOById()");
        return feedDTO;
    }

    public Optional<FeedPage> getFeedPageByTranslatePageId(Long id) {
        log.info("-> start execution method getFeedPageByTranslatePageId() by id: " + id);
        Optional<FeedPage> byTranslatePageId = feedPageRepo.findByTranslatePageId(id);
        log.info("-> exit from method getFeedPageByTranslatePageId(), return optional isPresent: " + byTranslatePageId.isPresent());
        return byTranslatePageId;
    }
}
