package com.kino_cms.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    private LocalDateTime createTime;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserDetails userDetails;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> userList;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserSession> userSessionList;

}
