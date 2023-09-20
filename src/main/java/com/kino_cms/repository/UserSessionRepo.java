package com.kino_cms.repository;

import com.kino_cms.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserSessionRepo extends JpaRepository<UserSession, Long> {

    Optional<UserSession> findByHttpSessionId(String httpSessionId);
    List<UserSession> getUserSessionsByOpenSessionAfterAndOpenSessionBefore(LocalDateTime after, LocalDateTime before);
}
