package com.kino_cms.service;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.entity.Cinema;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.enums.Language;
import com.kino_cms.repository.CinemaRepo;
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
public class CinemaService {

    private final CinemaRepo cinemaRepo;
    private final SaveUploadFileUtils uploadService;

    public Optional<Cinema> getCinemaById(Long id) {
        log.info(String.format("-> start execution method getCinemaById(id - %s)", id));
        Optional<Cinema> byId = cinemaRepo.findById(id);
        log.info("-> exit from method getCinemaById(), return cinema isPresent - " + byId.isPresent());
        return byId;
    }

    public List<CinemaDTO> getAllCinemaDto() {
        log.info("-> start execution method getAllCinemaDto()");
        Locale locale = LocaleContextHolder.getLocale();
        if (locale.getLanguage().equals("en")) {
            log.info("-> exit from method getAllCinemaDto(), return list for locale en");
            return cinemaRepo.getAllCinemaDto(Language.ENGLISH);
        }
        log.info("-> exit from method getAllCinemaDto(), return list for locale uk");
        return cinemaRepo.getAllCinemaDto(Language.UKRAINIAN);
    }

    public void deleteCinemaById(Long id) {
        log.info(String.format("-> start execution method deleteCinemaById(id %s)", id));
        Optional<Cinema> cinema = getCinemaById(id);
        if (cinema.isPresent()) {
            cinemaRepo.delete(cinema.get());
            log.info("Success deleting Cinema with id: " + id);
            List<String> listImagesFileName = getListImagesFileNameById(id);
            uploadService.deleteUploadFiles(listImagesFileName);
        } else {
            log.info(String.format("Fail deleted Cinema with id: %s, optional isEmpty", id));
        }
    }

    public Cinema saveCinema(Cinema cinema) {
        log.info("-> start execution method saveCinema()");
        Cinema save = cinemaRepo.save(cinema);
        if (cinema.getTranslatePageId() != null) {
            log.info(String.format("-> Success saving Cinema with id: %s", save.getId()));
            Cinema getTranslatePage = cinemaRepo.findById(save.getTranslatePageId()).get();
            getTranslatePage.setTranslatePageId(save.getId());
            Cinema save1 = cinemaRepo.save(getTranslatePage);
            log.info(String.format("-> Success update translationPage for Cinema with id: %s", getTranslatePage.getId()));
            return save1;
        } else {
            log.info("-> Success saving Cinema with id: %s");
            return save;
        }
    }

    public Optional<CinemaDTO> getCinemaDtoById(Long id) {
        log.info(String.format("-> start execution method getCinemaDtoById(id %s)", id));
        Optional<CinemaDTO> cinemaDtoById = cinemaRepo.getCinemaDtoById(id);
        log.info("-> exit from method getCinemaDtoById(), this cinema isPresent: " + cinemaDtoById.isPresent());
        return cinemaDtoById;
    }

    public CinemaDTO getPresentCinemaDtoById(Long id) {
        log.info(String.format("-> start execution method getPresentCinemaDtoById(id %s)", id));
        Optional<CinemaDTO> cinemaDtoById = cinemaRepo.getCinemaDtoById(id);
        CinemaDTO cinemaDTO = cinemaDtoById.orElseGet(CinemaDTO::new);
        log.info(String.format("-> exit from method getPresentCinemaDtoById(id %s), cinema isPresent: %s",
                id, cinemaDtoById.isPresent()));
        return cinemaDTO;
    }

    public Cinema saveCinemaDto(CinemaDTO cinemaDTO) {
        log.info(String.format("-> start execution method saveCinemaDto(Cinema %s)", cinemaDTO.toString()));
        Optional<Cinema> cinemaOptional = getCinemaById(cinemaDTO.getId());
        Cinema cinemaToSave;
        SeoBlock seoBlock;
        if (cinemaOptional.isPresent()) {
            log.info("-> saveCinemaDto > cinema isPresent");
            cinemaToSave = cinemaOptional.get();
            seoBlock = cinemaToSave.getSeoBlock();
        } else {
            log.info("-> saveCinemaDto > cinema isEmpty, create new Cinema/SeoBlock");
            cinemaToSave = new Cinema();
            seoBlock = new SeoBlock();
        }

        cinemaToSave.setName(cinemaDTO.getName());
        cinemaToSave.setDescription(cinemaDTO.getDescription());
        cinemaToSave.setConditions(cinemaDTO.getConditions());
        cinemaToSave.setFirstBanner(cinemaDTO.getFirstBanner());
        cinemaToSave.setLogoImage(cinemaDTO.getLogoImage());
        cinemaToSave.setImage1(cinemaDTO.getImage1());
        cinemaToSave.setImage2(cinemaDTO.getImage2());
        cinemaToSave.setImage3(cinemaDTO.getImage3());
        cinemaToSave.setImage4(cinemaDTO.getImage4());
        cinemaToSave.setImage5(cinemaDTO.getImage5());

        seoBlock.setSeoUrl(cinemaDTO.getSeoUrl());
        seoBlock.setSeoTitle(cinemaDTO.getSeoTitle());
        seoBlock.setSeoKeywords(cinemaDTO.getSeoKeywords());
        seoBlock.setSeoDescription(cinemaDTO.getSeoDescription());

        cinemaToSave.setSeoBlock(seoBlock);
        cinemaToSave.setLanguage(cinemaDTO.getLanguage());
        cinemaToSave.setTranslatePageId(cinemaDTO.getTranslatePageId());

        Cinema cinema = saveCinema(cinemaToSave);
        log.info("-> exit from method saveCinemaDto()");
        return cinema;
    }

    public List<String> getListImagesFileNameById(Long id) {
        log.info(String.format("-> start execution method getListImagesFileNameById(Id %s)", id));
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", "", ""));
        Cinema cinema;
        Optional<Cinema> cinemaOptional = getCinemaById(id);
        if (cinemaOptional.isPresent()) {
            cinema = cinemaOptional.get();
            log.info("-> getListImagesFileNameById > cinema isPresent, get image from Cinema");

            fileNamesFromDB.set(0, cinema.getLogoImage());
            fileNamesFromDB.set(1, cinema.getFirstBanner());
            fileNamesFromDB.set(2, cinema.getImage1());
            fileNamesFromDB.set(3, cinema.getImage2());
            fileNamesFromDB.set(4, cinema.getImage3());
            fileNamesFromDB.set(5, cinema.getImage4());
            fileNamesFromDB.set(6, cinema.getImage5());
        }
        log.info("-> exit from method getListImagesFileNameById()");
        return fileNamesFromDB;
    }

    public CinemaDTO updateImagesOnModel(CinemaDTO cinemaDTO, List<String> fileNamesFromDB) {
        log.info(String.format("-> start execution private method updateImagesOnModel(CinemaDTO %s)", cinemaDTO.toString()));
        cinemaDTO.setLogoImage(fileNamesFromDB.get(0));
        cinemaDTO.setFirstBanner(fileNamesFromDB.get(1));
        cinemaDTO.setImage1(fileNamesFromDB.get(2));
        cinemaDTO.setImage2(fileNamesFromDB.get(3));
        cinemaDTO.setImage3(fileNamesFromDB.get(4));
        cinemaDTO.setImage4(fileNamesFromDB.get(5));
        cinemaDTO.setImage5(fileNamesFromDB.get(6));
        log.info("-> success update image filename \n\t\t exit from method updateImagesOnModel()");
        return cinemaDTO;
    }

    public Optional<CinemaDTO> getCinemaByTranslatePageId(Long id) {
        log.info(String.format("-> start execution private method getCinemaByTranslatePageId(id %s)", id));
        Optional<CinemaDTO> cinemaDtoByTranslatePageId = cinemaRepo.getCinemaDtoByTranslatePageId(id);
        log.info("-> exit from method getCinemaByTranslatePageId, optional isPresent: " + cinemaDtoByTranslatePageId.isPresent());
        return cinemaDtoByTranslatePageId;
    }
}
