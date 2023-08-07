package com.kino_cms.service;

import com.kino_cms.entity.FeedPage;
import com.kino_cms.repository.FeedPageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedPageService {
    @Autowired
    FeedPageRepo feedPageRepo;

    public Optional<FeedPage> getFeedPageById(Long id){
        return feedPageRepo.findById(id);
    }
    public List<FeedPage> getAllFeedPages(){
        return feedPageRepo.findAll();
    }
    public void saveFeedPage(FeedPage feedPage){
        feedPageRepo.save(feedPage);
    }
    public void deleteFeedPage(FeedPage feedPage){
        feedPageRepo.delete(feedPage);
    }
}
