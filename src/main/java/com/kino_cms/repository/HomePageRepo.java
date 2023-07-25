package com.kino_cms.repository;

import com.kino_cms.entity.HomePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomePageRepo extends JpaRepository<HomePage, Long> {
}
