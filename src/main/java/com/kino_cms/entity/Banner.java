package com.kino_cms.entity;

import com.kino_cms.enums.BannerType;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private BannerType bannerType;
    private Integer slideSpeed;
    private String backgroundColor;
    @OneToMany(mappedBy = "banner", cascade = CascadeType.ALL)
    private List<BannerImage> bannerImages;
    private Boolean isActivate;
}
