package com.kino_cms.service;

import com.kino_cms.dto.GeneralPageDTO;
import com.kino_cms.entity.GeneralPage;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.repository.GeneralPageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GeneralPageService {
    @Autowired
    GeneralPageRepo generalPageRepo;

    public Optional<GeneralPageDTO> getGeneralPageById(Long id) {
        return generalPageRepo.getGeneralPageById(id);
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

        seoBlock.setSeoUrl(generalPageDTO.getSeoUrl());
        seoBlock.setSeoTitle(generalPageDTO.getSeoTitle());
        seoBlock.setSeoKeywords(generalPageDTO.getSeoKeywords());
        seoBlock.setSeoDescription(generalPageDTO.getSeoDescription());

        generalPage.setSeoBlock(seoBlock);

        generalPageRepo.save(generalPage);
    }
}
