package com.kino_cms.service;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.GeneralPageRepo;
import com.kino_cms.utils.SaveUploadFileUtils;
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
public class GeneralPageService {
    private final GeneralPageRepo generalPageRepo;
    private final SaveUploadFileUtils uploadFileUtils;

    public Optional<GeneralPageDTO> getGeneralPageDTOById(Long id) {
        log.info("-> start execution method getGeneralPageDTOById by id: " + id);
        Optional<GeneralPageDTO> generalPageDTO = generalPageRepo.getGeneralPageDTOById(id);
        log.info("-> exit from method getGeneralPageDTOById(), return optional isPresent: " + generalPageDTO.isPresent());
        return generalPageDTO;
    }

    public Optional<GeneralPage> findById(Long id) {
        log.info("-> start execution method findById by id: " + id);
        Optional<GeneralPage> byId = generalPageRepo.findById(id);
        log.info("-> exit from method findById(), return optional isPresent: " + byId.isPresent());
        return byId;
    }

    public void saveGeneralPageDTO(GeneralPageDTO generalPageDTO) {
        log.info(String.format("-> start execution method saveGeneralPageDTO(GeneralPageDTO %s)", generalPageDTO));
        Optional<GeneralPage> generalPageGet = generalPageRepo.findById(generalPageDTO.getId());
        GeneralPage generalPage;
        SeoBlock seoBlock;
        if (generalPageGet.isPresent()) {
            log.info("-> saveGeneralPageDTO > generalPageDTO isPresent");
            generalPage = generalPageGet.get();
            seoBlock = generalPage.getSeoBlock();
        } else {
            log.info("-> saveGeneralPageDTO > generalPageDTO isEmpty, create new GeneralPage/SeoBlock");
            generalPage = new GeneralPage();
            generalPage.setId(0L);
            seoBlock = new SeoBlock();
        }

        generalPage.setPageType(generalPageDTO.getPageType());
        generalPage.setTitle(generalPageDTO.getTitle());
        generalPage.setDescription(generalPageDTO.getDescription());
        generalPage.setMainImage(generalPageDTO.getMainImage());
        generalPage.setImage1(generalPageDTO.getImage1());
        generalPage.setImage2(generalPageDTO.getImage2());
        generalPage.setImage3(generalPageDTO.getImage3());
        generalPage.setImage4(generalPageDTO.getImage4());
        generalPage.setImage5(generalPageDTO.getImage5());
        generalPage.setIsActive(generalPageDTO.getIsActive());
        generalPage.setCreateTime(generalPageDTO.getCreateTime());

        generalPage.setTranslatePageId(generalPageDTO.getTranslatePageId());
        generalPage.setLanguage(generalPageDTO.getLanguage());

        seoBlock.setSeoUrl(generalPageDTO.getSeoUrl());
        seoBlock.setSeoTitle(generalPageDTO.getSeoTitle());
        seoBlock.setSeoKeywords(generalPageDTO.getSeoKeywords());
        seoBlock.setSeoDescription(generalPageDTO.getSeoDescription());

        generalPage.setSeoBlock(seoBlock);

        updateTranslateLink(generalPageRepo.save(generalPage));
        log.info("-> exit from method saveGeneralPageDTO()");
    }

    private void updateTranslateLink(GeneralPage save) {
        log.info("-> start execution method updateTranslateLink() on id: " + save.getId());
        if (save.getTranslatePageId() != null) {
            log.info("-> updateTranslateLink() > GeneralPage has TranslatePageId");
            Optional<GeneralPage> generalPage = generalPageRepo.findById(save.getTranslatePageId());
            if (generalPage.isPresent()) {
                log.info("-> updateTranslateLink() > GeneralPage has TranslatePage with id: " + generalPage.get().getId());
                GeneralPage generalPage1 = generalPage.get();
                generalPage1.setTranslatePageId(save.getId());
                generalPageRepo.save(generalPage1);
                log.info("-> updateTranslateLink() > GeneralPage success update TranslatePage");
            }
        }
    }

    public void delete(GeneralPage generalPage) {
        log.info("-> start execution method delete(GeneralPage) with id: " + generalPage.getId());
        List<String> imageToDelete = new ArrayList<>(List.of(generalPage.getMainImage(), generalPage.getImage1(),
                generalPage.getImage2(), generalPage.getImage3(),
                generalPage.getImage4(), generalPage.getImage5()));

        log.info("-> delete(GeneralPage)");
        generalPageRepo.deleteById(generalPage.getId());
        log.info("-> deleteUploadFiles(GeneralPage) with id: " + generalPage.getId());
        uploadFileUtils.deleteUploadFiles(imageToDelete);
        log.info("-> exit from method delete(GeneralPage)");
    }

    public void save(GeneralPage generalPageModel) {
        log.info("-> start execution method save(GeneralPage)");
        generalPageRepo.save(generalPageModel);
        log.info("-> exit from method save(GeneralPage)");
    }

    public List<GeneralPageDTO> getAllOtherPages() {
        log.info("-> start execution method getAllOtherPages()");
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().equals("en")) {
            log.info("-> return list GeneralPageDTO by locale: " + locale);
            return generalPageRepo.getAllByPageTypeOtherPage(Language.ENGLISH);
        }
        log.info("-> return list GeneralPageDTO by locale: " + locale);
        return generalPageRepo.getAllByPageTypeOtherPage(Language.UKRAINIAN);
    }

    public GeneralPageDTO getGeneralPageDTOAboutCinema() {
        log.info("-> start execution method getGeneralPageDTOAboutCinema()");
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().equals("en")) {
            log.info("-> return list GeneralPageDTO by locale: " + locale);
            return generalPageRepo.getAboutCinemaPageUk(Language.ENGLISH);
        }
        log.info("-> return list GeneralPageDTO by locale: " + locale);
        return generalPageRepo.getAboutCinemaPageUk(Language.UKRAINIAN);
    }

    public List<GeneralPage> getAllPageByPageTypeForMenu(Locale locale) {
        log.info("-> start execution method getAllPageByPageTypeForMenu()");
        if (locale.getLanguage().equals("en")) {
            log.info("-> return list GeneralPageDTO by locale: " + locale);
            return generalPageRepo.getAllUkPageByPageTypeUnion(Language.ENGLISH);
        }
        log.info("-> return list GeneralPageDTO by locale: " + locale);
        return generalPageRepo.getAllUkPageByPageTypeUnion(Language.UKRAINIAN);
    }

    public Optional<GeneralPageDTO> getGeneralPageDTOByLanguagePageId(Long id) {
        log.info("-> start execution method getGeneralPageDTOByLanguagePageId()");
        Optional<GeneralPageDTO> generalPageDTOByLanguagePageId =
                generalPageRepo.getGeneralPageDTOByLanguagePageId(id);
        log.info("-> exit from method getGeneralPageDTOByLanguagePageId, optional isPresent: "
                + generalPageDTOByLanguagePageId.isPresent());
        return generalPageDTOByLanguagePageId;
    }

    public List<String> getListImagesFileNameById(Long id) {
        log.info(String.format("-> start execution method getListImagesFileNameById(Id %s)", id));
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", ""));
        GeneralPage generalPage;
        Optional<GeneralPage> generalPageOptional = generalPageRepo.findById(id);
        if (generalPageOptional.isPresent()) {
            generalPage = generalPageOptional.get();
            log.info("-> getListImagesFileNameById > GeneralPage isPresent, get image from GeneralPage");
            fileNamesFromDB.set(0, generalPage.getMainImage());
            fileNamesFromDB.set(1, generalPage.getImage1());
            fileNamesFromDB.set(2, generalPage.getImage2());
            fileNamesFromDB.set(3, generalPage.getImage3());
            fileNamesFromDB.set(4, generalPage.getImage4());
            fileNamesFromDB.set(5, generalPage.getImage5());
        }
        log.info("-> exit from method getListImagesFileNameById()");
        return fileNamesFromDB;
    }
}

