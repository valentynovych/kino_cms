package com.kino_cms.service;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.dto.HallDTO;
import com.kino_cms.entity.Cinema;
import com.kino_cms.entity.Hall;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.repository.HallRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HallService {
    @Autowired
    HallRepo hallRepo;
    @Autowired
    CinemaService cinemaService;
    @Autowired
    SaveUploadService uploadService;

    public Optional<Hall> getHallById(Long id) {
        return hallRepo.findById(id);
    }

    public List<Hall> getAllHalls() {
        return hallRepo.findAll();
    }

    public void saveHall(Hall hall) {
        Optional<Cinema> cinemaById = cinemaService.getCinemaById(hall.getCinema().getId());
        if (cinemaById.isPresent()) {
            Cinema cinema = cinemaById.get();
            // check of cinema and hall languages is equals
            if (!cinema.getLanguage().equals(hall.getLanguage())) {
                Cinema cinema1 = cinemaService.getCinemaById(cinema.getTranslatePageId()).get();
                hall.setCinema(cinema1);
            }
        }

        if (hall.getTranslatePageId() != null) {
            Hall save = hallRepo.save(hall);
            Hall toSetTranslate = hallRepo.findById(save.getTranslatePageId()).get();
            toSetTranslate.setTranslatePageId(save.getId());
            hallRepo.save(toSetTranslate);
        } else {
            hallRepo.save(hall);
        }
    }

    public void deleteHall(Long id) {
        Optional<Hall> hallOptional = getHallById(id);
        if (hallOptional.isPresent()) {
            hallRepo.delete(hallOptional.get());
            List<String> listImagesFileName = getListImagesFileNameById(id);
            uploadService.deleteUploadFiles(listImagesFileName);
        }
    }

    public Optional<HallDTO> getHallDtoById(Long id) {
        return hallRepo.getHallDtoById(id);
    }

    public List<HallDTO> getAllHallByCinema(CinemaDTO cinemaDTO) {
        Cinema cinema = new Cinema();
        cinema.setId(cinemaDTO.getId());
        return hallRepo.getAllHallByCinema(cinema);
    }

    public List<String> getListImagesFileNameById(Long id) {
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", "", ""));
        Hall hall;
        Optional<Hall> hallOptional = getHallById(id);
        if (hallOptional.isPresent()) {
            hall = hallOptional.get();

            fileNamesFromDB.set(0, hall.getHallSchema());
            fileNamesFromDB.set(1, hall.getFirstBanner());
            fileNamesFromDB.set(2, hall.getImage1());
            fileNamesFromDB.set(3, hall.getImage2());
            fileNamesFromDB.set(4, hall.getImage3());
            fileNamesFromDB.set(5, hall.getImage4());
            fileNamesFromDB.set(6, hall.getImage5());
        }
        return fileNamesFromDB;
    }

    public HallDTO updateImagesOnModel(HallDTO hallDTOModel, List<String> fileNamesFromDB) {

        hallDTOModel.setHallSchema(fileNamesFromDB.get(0));
        hallDTOModel.setFirstBanner(fileNamesFromDB.get(1));
        hallDTOModel.setImage1(fileNamesFromDB.get(2));
        hallDTOModel.setImage2(fileNamesFromDB.get(3));
        hallDTOModel.setImage3(fileNamesFromDB.get(4));
        hallDTOModel.setImage4(fileNamesFromDB.get(5));
        hallDTOModel.setImage5(fileNamesFromDB.get(6));
        return hallDTOModel;
    }

    public Long getCinemaIdByHall(Long id) {
        Optional<Hall> hallOptional = getHallById(id);
        if (hallOptional.isPresent()) {
            return hallOptional.get().getCinema().getId();
        } else {
            return null;
        }
    }

    public void saveHallDTO(HallDTO hallDTOModel) {
        Optional<Hall> hallOptional = getHallById(hallDTOModel.getId());
        Hall hallToSave;
        SeoBlock seoBlock;
        if (hallOptional.isPresent()) {
            hallToSave = hallOptional.get();
            seoBlock = hallToSave.getSeoBlock();
        } else {
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
    }

    public Optional<HallDTO> getHallDtoByTranslatePageId(Long id) {
        return hallRepo.findByTranslatePageId(id);
    }
}
