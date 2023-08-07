package com.kino_cms.repository;


import com.kino_cms.entity.ContactPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactPageRepo extends JpaRepository<ContactPage, Long> {

}
