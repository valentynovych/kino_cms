package com.kino_cms.service;

import com.kino_cms.dto.Page;
import com.kino_cms.repository.ContactPageRepo;
import com.kino_cms.repository.HomePageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageService {
    @Autowired
    GeneralPageService generalPageRepo;
    @Autowired
    HomePageRepo homePageRepo;
    @Autowired
    ContactPageRepo contactPageRepo;

    public List<Page> getAllPages() {
        List<Page> allPages = new ArrayList<>();
        allPages.addAll(generalPageRepo.findAll());
        allPages.addAll(homePageRepo.findAll());
        allPages.addAll(contactPageRepo.findAll());
        return allPages;
    }
}
