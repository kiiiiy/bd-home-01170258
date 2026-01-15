package com.bd.homepage.domain.recruitment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "recruitment_settings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RecruitmentSettings {

    @Id
    private Long id; // 항상 1 고정

    @Column(length = 500)
    private String applyLink;

    @Column(nullable = false, length = 200)
    private String bannerTitle;

    @Column(columnDefinition = "TEXT")
    private String bannerDesc;

    @Column(length = 500)
    private String bannerImage;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void update(String applyLink, String bannerTitle, String bannerDesc, String bannerImage) {
        this.applyLink = applyLink;
        this.bannerTitle = bannerTitle;
        this.bannerDesc = bannerDesc;
        this.bannerImage = bannerImage;
        this.updatedAt = LocalDateTime.now();
    }
}
