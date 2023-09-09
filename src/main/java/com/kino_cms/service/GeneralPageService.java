package com.kino_cms.service;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.dto.Page;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.GeneralPageRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class GeneralPageService {
    @Autowired
    GeneralPageRepo generalPageRepo;

    Logger logger = LogManager.getLogger();
    @Value("${upload.path}")
    private String uploadPath;

    public Optional<GeneralPageDTO> getGeneralPageDTOById(Long id) {
        System.out.println("Find generalPage by id: " + id);
        Optional<GeneralPageDTO> generalPageDTO = generalPageRepo.getGeneralPageDTOById(id);
        System.out.println("finding generalPageDTO: " + generalPageDTO);
        return generalPageDTO;
    }

    public Optional<GeneralPage> findById(Long id) {
        return generalPageRepo.findById(id);
    }

    public void deleteGeneralPageById(Long id) {
        generalPageRepo.deleteById(id);
    }

    public void saveGeneralPageDTO(GeneralPageDTO generalPageDTO) {
        Optional<GeneralPage> generalPageGet = generalPageRepo.findById(generalPageDTO.getId());
        GeneralPage generalPage;
        SeoBlock seoBlock;
        if (generalPageGet.isPresent()) {
            generalPage = generalPageGet.get();
            seoBlock = generalPage.getSeoBlock();
        } else {
            generalPage = new GeneralPage();
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

        seoBlock.setSeoUrl(generalPageDTO.getSeoUrl());
        seoBlock.setSeoTitle(generalPageDTO.getSeoTitle());
        seoBlock.setSeoKeywords(generalPageDTO.getSeoKeywords());
        seoBlock.setSeoDescription(generalPageDTO.getSeoDescription());

        generalPage.setSeoBlock(seoBlock);

        generalPageRepo.save(generalPage);
    }

    public void delete(GeneralPage generalPage) {
        List<String> imageToDelete = new ArrayList<>(List.of(generalPage.getMainImage(), generalPage.getImage1(),
                generalPage.getImage2(), generalPage.getImage3(),
                generalPage.getImage4(), generalPage.getImage5()));

        imageToDelete.forEach(this::deleteImage);

        generalPageRepo.deleteById(generalPage.getId());
    }

    public List<GeneralPage> findAll() {
        return generalPageRepo.findAll();
    }

    public void save(GeneralPage generalPageModel) {
        generalPageRepo.save(generalPageModel);
    }

    public void deleteImage(String fileName) {
        File image = new File(uploadPath + "/" + fileName);
        if (image.exists()) {
            if (image.delete()) {
                logger.info("Method - deleteImage | file:" + image.getAbsolutePath() + "has deleted");
            } else {
                logger.error("Method - deleteImage | file:" + image.getAbsolutePath() + "error deleted");
            }
        } else {
            logger.error("Method - deleteImage | file:" + image.getAbsolutePath() + "not exists in filesystem");
        }
    }

    public List<GeneralPageDTO> getAllOtherPages(Locale locale) {
        List<GeneralPageDTO> otherPages;
        if (locale.getLanguage().equals("en")) {
            return generalPageRepo.getAllByPageTypeOtherPage(Language.ENGLISH);
        }
        return generalPageRepo.getAllByPageTypeOtherPage(Language.UKRAINIAN);
    }

    public List<GeneralPage> getAllUkPageByPageTypeForMenu(){
        List<GeneralPage> ukPageByPageTypeUnion = generalPageRepo.getAllUkPageByPageTypeUnion(Language.UKRAINIAN);
        return ukPageByPageTypeUnion;
    }

    public GeneralPageDTO getGeneralPageDTOAboutCinema() {
        GeneralPageDTO aboutCinemaPage = generalPageRepo.getAboutCinemaPageUk();
        return aboutCinemaPage;
    }

    public List<GeneralPage> getAllPageByPageTypeForMenu(Locale locale) {
        if (locale.getLanguage().equals("en")) {
            return null;
        }
        return generalPageRepo.getAllUkPageByPageTypeUnion(Language.UKRAINIAN);
    }
}

