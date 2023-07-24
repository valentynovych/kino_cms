package com.kino_cms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;
    private Timestamp createTime;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserDetails userDetails;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> tickets;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserSession> userSessionList;
}
