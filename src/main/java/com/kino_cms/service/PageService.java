package com.kino_cms.service;

import com.kino_cms.dto.Page;
import com.kino_cms.repository.ContactPageRepo;
import com.kino_cms.repository.GeneralPageRepo;
import com.kino_cms.repository.HomePageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PageService {

    private final GeneralPageRepo generalPageRepo;
    private final HomePageRepo homePageRepo;
    private final ContactPageRepo contactPageRepo;

    public List<Page> getAllPages() {
        log.info("-> start execute method getAllPages()");
        List<Page> allPages = new ArrayList<>();
        allPages.addAll(generalPageRepo.findAll());
        allPages.addAll(homePageRepo.findAll());
        allPages.addAll(contactPageRepo.findAll());
        log.info("-> exit from method getAllPages(), returned pages count: " + allPages.size());
        return allPages;
    }
}
