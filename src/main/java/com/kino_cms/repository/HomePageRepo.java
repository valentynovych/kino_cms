package com.kino_cms.repository;

import com.kino_cms.entity.HomePage;
import com.kino_cms.enums.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HomePageRepo extends JpaRepository<HomePage, Long> {
    Optional<HomePage> findByLanguage(Language language);
}
