package com.bd.homepage.domain.winner.repository;

import com.bd.homepage.domain.winner.entity.WinnersSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinnersSettingsRepository extends JpaRepository<WinnersSettings, Long> {
}
