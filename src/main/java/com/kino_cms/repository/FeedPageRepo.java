package com.kino_cms.repository;

import com.kino_cms.entity.FeedPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedPageRepo extends JpaRepository<FeedPage, Long> {

}
