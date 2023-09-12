package com.kino_cms.service;

import com.kino_cms.dto.FeedDTO;
import com.kino_cms.entity.FeedPage;
import com.kino_cms.enums.FeedType;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.FeedPageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class FeedPageService {
    @Autowired
    FeedPageRepo feedPageRepo;
    @Autowired
    SaveUploadService uploadService;

    public Optional<FeedPage> getFeedPageById(Long id) {
        return feedPageRepo.findById(id);
    }

    public List<FeedPage> getAllFeedPages() {
        return feedPageRepo.findAll();
    }

    public void saveFeedPage(FeedPage feedPage) {
        if (feedPage.getTranslatePageId() != null) {
            FeedPage feedPage1 = feedPageRepo.save(feedPage);
            FeedPage toSetTranslate = feedPageRepo.findById(feedPage.getTranslatePageId()).get();
            toSetTranslate.setTranslatePageId(feedPage1.getId());
            feedPageRepo.save(toSetTranslate);
        } else {
            feedPageRepo.save(feedPage);
        }
    }

    public void deleteFeedPage(FeedPage feedPage) {
        feedPageRepo.delete(feedPage);
    }

    public List<FeedPage> findFeedPagesByFeedType(FeedType feedType) {
        return feedPageRepo.findFeedPagesByFeedType(feedType);
    }

    public List<String> getListImagesFileNameById(Long id) {
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", ""));
        FeedPage feedPage;

        Optional<FeedPage> feedPageOptional = getFeedPageById(id);

        if (feedPageOptional.isPresent()) {
            feedPage = feedPageOptional.get();

            fileNamesFromDB.set(0, feedPage.getMainImage());
            fileNamesFromDB.set(1, feedPage.getImage1());
            fileNamesFromDB.set(2, feedPage.getImage2());
            fileNamesFromDB.set(3, feedPage.getImage3());
            fileNamesFromDB.set(4, feedPage.getImage4());
            fileNamesFromDB.set(5, feedPage.getImage5());
        }

        return fileNamesFromDB;
    }

    public FeedPage updateImagesOnModel(FeedPage feedPageModel, Long id, List<String> fileNamesFromDB) {

        feedPageModel.setMainImage(fileNamesFromDB.get(0));
        feedPageModel.setImage1(fileNamesFromDB.get(1));
        feedPageModel.setImage2(fileNamesFromDB.get(2));
        feedPageModel.setImage3(fileNamesFromDB.get(3));
        feedPageModel.setImage4(fileNamesFromDB.get(4));
        feedPageModel.setImage5(fileNamesFromDB.get(5));
        return feedPageModel;
    }

    public List<FeedDTO> getAllFeedDTOByFeedTypeFeed() {
        Locale locale = LocaleContextHolder.getLocale();
        List<FeedDTO> allFeedDTO;
        if (locale.getLanguage().equals("en")) {
            allFeedDTO = feedPageRepo.getAllFeedDTOByTypeFeed(Language.ENGLISH);
        } else {
            allFeedDTO = feedPageRepo.getAllFeedDTOByTypeFeed(Language.UKRAINIAN);
        }
        return allFeedDTO;
    }

    public List<FeedDTO> getAllFeedDTOByFeedTypePromotion() {
        Locale locale = LocaleContextHolder.getLocale();
        List<FeedDTO> allFeedDTO;
        if (locale.getLanguage().equals("en")) {
            allFeedDTO = feedPageRepo.getAllFeedDTOByTypePromotion(Language.ENGLISH);
        } else {
            allFeedDTO = feedPageRepo.getAllFeedDTOByTypePromotion(Language.UKRAINIAN);
        }
        return allFeedDTO;
    }

    public FeedDTO getFeedDTOById(Long feedId) {
        FeedDTO feedDTO = feedPageRepo.getFeedDTOById(feedId);
        return feedDTO;
    }

    public Optional<FeedPage> getFeedPageByTranslatePageId(Long id) {
        return feedPageRepo.findByTranslatePageId(id);
    }
}
