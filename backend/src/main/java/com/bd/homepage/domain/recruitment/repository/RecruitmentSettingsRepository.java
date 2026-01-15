package com.bd.homepage.domain.recruitment.repository;

import com.bd.homepage.domain.recruitment.entity.RecruitmentSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentSettingsRepository
        extends JpaRepository<RecruitmentSettings, Long> {
}
