package com.kino_cms.repository;

import com.kino_cms.entity.MailingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingRepository extends JpaRepository<MailingEntity, Long> {

}
