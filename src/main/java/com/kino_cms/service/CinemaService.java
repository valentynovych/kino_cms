package com.kino_cms.service;

import com.kino_cms.dto.CinemaDTO;
import com.kino_cms.entity.Cinema;
import com.kino_cms.entity.SeoBlock;
import com.kino_cms.repository.CinemaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CinemaService {
    @Autowired
    CinemaRepo cinemaRepo;
    @Autowired
    SaveUploadService uploadService;

    public Optional<Cinema> getCinemaById(Long id) {
        return cinemaRepo.findById(id);
    }

    public List<CinemaDTO> getAllCinemaDto() {
        return cinemaRepo.getAllCinemaDto();
    }

    public void deleteCinemaById(Long id) {
        Optional<Cinema> cinema = getCinemaById(id);
        if (cinema.isPresent()) {
            cinemaRepo.delete(cinema.get());
            List<String> listImagesFileName = getListImagesFileNameById(id);
            uploadService.deleteUploadFiles(listImagesFileName);
        }
    }

    public void saveCinema(Cinema cinema) {
        cinemaRepo.save(cinema);
    }

    public Optional<CinemaDTO> getCinemaDtoById(Long id) {
        return cinemaRepo.getCinemaDtoById(id);
    }

    public CinemaDTO getPresentCinemaDtoById(Long id) {
        Optional<CinemaDTO> cinemaDtoById = cinemaRepo.getCinemaDtoById(id);
        CinemaDTO cinemaDTO;
        cinemaDTO = cinemaDtoById.orElseGet(CinemaDTO::new);
        return cinemaDTO;
    }

    public void saveCinemaDto(CinemaDTO cinemaDTO) {
        Optional<Cinema> cinemaOptional = getCinemaById(cinemaDTO.getId());
        Cinema cinemaToSave;
        SeoBlock seoBlock;
        if (cinemaOptional.isPresent()) {
            cinemaToSave = cinemaOptional.get();
            seoBlock = cinemaToSave.getSeoBlock();
        } else {
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

        saveCinema(cinemaToSave);
    }

    public List<String> getListImagesFileNameById(Long id) {
        List<String> fileNamesFromDB = new ArrayList<>(List.of("", "", "", "", "", "", ""));
        Cinema cinema;
        Optional<Cinema> cinemaOptional = getCinemaById(id);
        if (cinemaOptional.isPresent()) {
            cinema = cinemaOptional.get();

            fileNamesFromDB.set(0, cinema.getLogoImage());
            fileNamesFromDB.set(1, cinema.getFirstBanner());
            fileNamesFromDB.set(2, cinema.getImage1());
            fileNamesFromDB.set(3, cinema.getImage2());
            fileNamesFromDB.set(4, cinema.getImage3());
            fileNamesFromDB.set(5, cinema.getImage4());
            fileNamesFromDB.set(6, cinema.getImage5());
        }
        return fileNamesFromDB;
    }

    public CinemaDTO updateImagesOnModel(CinemaDTO cinemaDTO, List<String> fileNamesFromDB) {

        cinemaDTO.setLogoImage(fileNamesFromDB.get(0));
        cinemaDTO.setFirstBanner(fileNamesFromDB.get(1));
        cinemaDTO.setImage1(fileNamesFromDB.get(2));
        cinemaDTO.setImage2(fileNamesFromDB.get(3));
        cinemaDTO.setImage3(fileNamesFromDB.get(4));
        cinemaDTO.setImage4(fileNamesFromDB.get(5));
        cinemaDTO.setImage5(fileNamesFromDB.get(6));
        return cinemaDTO;
    }
}
