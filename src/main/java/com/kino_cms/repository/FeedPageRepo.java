package com.kino_cms.repository;

import com.kino_cms.entity.FeedPage;
import com.kino_cms.enums.FeedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedPageRepo extends JpaRepository<FeedPage, Long> {



    List<FeedPage> findFeedPagesByFeedType(FeedType feedType);
}
