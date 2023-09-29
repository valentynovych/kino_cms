package com.kino_cms.repository;

import com.kino_cms.entity.BannerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerImageRepo extends JpaRepository<BannerImage, Long> {
}
