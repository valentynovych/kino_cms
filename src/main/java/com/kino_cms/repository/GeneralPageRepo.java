package com.kino_cms.repository;

import com.kino_cms.entity.GeneralPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeneralPageRepo extends JpaRepository<GeneralPage, Long> {

}
