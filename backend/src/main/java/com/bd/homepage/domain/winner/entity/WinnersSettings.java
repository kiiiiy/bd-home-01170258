package com.bd.homepage.domain.winner.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "winners_settings")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class WinnersSettings {

    @Id
    private Long id; // 1 고정 권장

    @Column(nullable = false)
    private boolean isPublic;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String notice;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    @PreUpdate
    void touch() {
        this.updatedAt = Instant.now();
    }

    public static WinnersSettings defaultRow() {
        return WinnersSettings.builder()
                .id(1L)
                .isPublic(false)
                .notice(null)
                .build();
    }
}
