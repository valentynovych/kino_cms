package com.kino_cms.entity;

import com.kino_cms.enums.BannerType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "banner_type")
    private BannerType bannerType;
    private Integer slideSpeed;
    private String backgroundColor;
    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL)
    private List<BannerImage> bannerImages;
    private Boolean isActivate;
}
