package com.kino_cms.service;

import com.kino_cms.entity.HomePage;
import com.kino_cms.repository.HomePageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class HomePageService {
    @Autowired
    HomePageRepo homePageRepo;

    public Optional<HomePage> getHomePageById(Long id) {
        return homePageRepo.findById(id);
    }

    public void save(HomePage homePage) {
        homePageRepo.save(homePage);
    }

}
