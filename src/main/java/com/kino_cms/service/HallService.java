package com.kino_cms.service;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.dto.HallDTO;
import com.kino_cms.entity.Cinema;
import com.kino_cms.entity.Hall;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.repository.HallRepo;
import com.kino_cms.utils.SaveUploadFileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class HallService {

    private final HallRepo hallRepo;
    private final CinemaService cinemaService;
    private final SaveUploadFileUtils uploadService;

    public Optional<Hall> getHallById(Long id) {
        log.info("-> start execution method getHallById by id: " + id);
        Optional<Hall> byId = hallRepo.findById(id);
        log.info("-> exit from method getHallById, return Hall isPresent " + byId.isPresent());
        return byId;
    }

    public void saveHall(Hall hall) {
        log.info("-> start execution method saveHall(): " + hall);
        Optional<Cinema> cinemaById = cinemaService.getCinemaById(hall.getCinema().getId());
        if (cinemaById.isPresent()) {
            log.info("-> Cinema isPresent, get from optional");
            Cinema cinema = cinemaById.get();
            // check of cinema and hall languages is equals
            if (!cinema.getLanguage().equals(hall.getLanguage())) {
                log.info("-> Cinema language is not equals Hall language, change Cinema on TranslatePageId");
                Cinema cinema1 = cinemaService.getCinemaById(cinema.getTranslatePageId()).get();
                hall.setCinema(cinema1);
            }
        }

        if (hall.getTranslatePageId() != null) {
            log.info("-> Hall has translatePage");
            Hall save = hallRepo.save(hall);
            log.info("-> save Hall, with id: " + save.getId());
            Hall toSetTranslate = hallRepo.findById(save.getTranslatePageId()).get();
            toSetTranslate.setTranslatePageId(save.getId());
            hallRepo.save(toSetTranslate);
            log.info("-> update translatePage for Hall id: " + toSetTranslate.getId());
        } else {
            hallRepo.save(hall);
            log.info("-> save Hall");
        }
        log.info("-> exit from method saveHall()");
    }

    public void deleteHall(Long id) {
        log.info("-> start execution method deleteHall() by id: " + id);
        Optional<Hall> hallOptional = getHallById(id);
        if (hallOptional.isPresent()) {
            log.info("-> Hall isPresent, start deleting");
            hallRepo.delete(hallOptional.get());
            List<String> listImagesFileName = getListImagesFileNameById(id);
            log.info("-> Hall has deleted, start deleting uploadsFile");
            uploadService.deleteUploadFiles(listImagesFileName);
        }
        log.info("-> exit from method deleteHall()");
    }

    public Optional<HallDTO> getHallDtoById(Long id) {
        log.info("-> start execution method getHallDtoById() by id: " + id);
        Optional<HallDTO> hallDtoById = hallRepo.getHallDtoById(id);
        log.info("-> exit from method getHallDtoById, return HallDTO isPresent " + hallDtoById.isPresent());
        return hallDtoById;
    }

    public List<HallDTO> getAllHallByCinema(CinemaDTO cinemaDTO) {
        log.info("-> start execution method getAllHallByCinema() by: " + cinemaDTO);
        Cinema cinema = new Cinema();
        cinema.setId(cinemaDTO.getId());
        List<HallDTO> allHallByCinema = hallRepo.getAllHallByCinema(cinema);
        log.info("-> exit from method getAllHallByCinema, return list size: " + allHallByCinema.size());
        return allHallByCinema;
    }

    public List<String> getListImagesFileNameById(Long id) {
        log.info(String.format("-> start execution method getListImagesFileNameById(Id %s)", id));
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", "", ""));
        Hall hall;
        Optional<Hall> hallOptional = getHallById(id);
        if (hallOptional.isPresent()) {
            hall = hallOptional.get();
            log.info("-> getListImagesFileNameById > hall isPresent, get image from Hall");
            fileNamesFromDB.set(0, hall.getHallSchema());
            fileNamesFromDB.set(1, hall.getFirstBanner());
            fileNamesFromDB.set(2, hall.getImage1());
            fileNamesFromDB.set(3, hall.getImage2());
            fileNamesFromDB.set(4, hall.getImage3());
            fileNamesFromDB.set(5, hall.getImage4());
            fileNamesFromDB.set(6, hall.getImage5());
        }
        log.info("-> exit from method getListImagesFileNameById()");
        return fileNamesFromDB;
    }

    public HallDTO updateImagesOnModel(HallDTO hallDTOModel, List<String> fileNamesFromDB) {
        log.info(String.format("-> start execution private method updateImagesOnModel(HallDTO %s)", hallDTOModel));
        hallDTOModel.setHallSchema(fileNamesFromDB.get(0));
        hallDTOModel.setFirstBanner(fileNamesFromDB.get(1));
        hallDTOModel.setImage1(fileNamesFromDB.get(2));
        hallDTOModel.setImage2(fileNamesFromDB.get(3));
        hallDTOModel.setImage3(fileNamesFromDB.get(4));
        hallDTOModel.setImage4(fileNamesFromDB.get(5));
        hallDTOModel.setImage5(fileNamesFromDB.get(6));
        log.info("-> success update image filename \n\t\t exit from method updateImagesOnModel()");
        return hallDTOModel;
    }

    public Long getCinemaIdByHall(Long id) {
        log.info("-> start execution method getCinemaIdByHall() by id: " + id);
        Optional<Hall> hallOptional = getHallById(id);
        if (hallOptional.isPresent()) {
            log.info("-> hallOptional isPresent");
            return hallOptional.get().getCinema().getId();
        } else {
            log.info("-> hallOptional isEmpty, return id: 0L");
            return 0L;
        }
    }

    public void saveHallDTO(HallDTO hallDTOModel) {
        log.info("-> start execution method saveHallDTO()");
        Optional<Hall> hallOptional = getHallById(hallDTOModel.getId());
        Hall hallToSave;
        SeoBlock seoBlock;
        if (hallOptional.isPresent()) {
            log.info("-> hallOptional isPresent, get Hall/SeoBlock");
            hallToSave = hallOptional.get();
            seoBlock = hallToSave.getSeoBlock();
        } else {
            log.info("-> hallOptional isEmpty, create new Hall/SeoBlock");
            hallToSave = new Hall();
            Cinema cinema = new Cinema();
            cinema.setId(hallDTOModel.getCinemaId());
            hallToSave.setCinema(cinema);
            seoBlock = new SeoBlock();
        }

        hallToSave.setCreateTime(hallDTOModel.getCreateTime());
        hallToSave.setName(hallDTOModel.getName());
        hallToSave.setDescriptions(hallDTOModel.getDescriptions());
        hallToSave.setHallSchema(hallDTOModel.getHallSchema());
        hallToSave.setFirstBanner(hallDTOModel.getFirstBanner());


        seoBlock.setSeoUrl(hallDTOModel.getSeoUrl());
        seoBlock.setSeoTitle(hallDTOModel.getSeoTitle());
        seoBlock.setSeoKeywords(hallDTOModel.getSeoKeywords());
        seoBlock.setSeoDescription(hallDTOModel.getSeoDescription());
        hallToSave.setSeoBlock(seoBlock);

        hallToSave.setImage1(hallDTOModel.getImage1());
        hallToSave.setImage2(hallDTOModel.getImage2());
        hallToSave.setImage3(hallDTOModel.getImage3());
        hallToSave.setImage4(hallDTOModel.getImage4());
        hallToSave.setImage5(hallDTOModel.getImage5());
        hallToSave.setLanguage(hallDTOModel.getLanguage());
        hallToSave.setTranslatePageId(hallDTOModel.getTranslatePageId());

        saveHall(hallToSave);
        log.info("-> exit from method saveHallDTO()");
    }

    public Optional<HallDTO> getHallDtoByTranslatePageId(Long id) {
        log.info(String.format("-> start execution private method getHallDtoByTranslatePageId(id %s)", id));
        Optional<HallDTO> byTranslatePageId = hallRepo.findByTranslatePageId(id);
        log.info("-> exit from method getHallDtoByTranslatePageId, optional isPresent: " + byTranslatePageId.isPresent());
        return byTranslatePageId;
    }
}
