package com.bd.homepage.domain.recruitment.service;

import com.bd.homepage.domain.recruitment.dto.RecruitmentSettingsResponse;
import com.bd.homepage.domain.recruitment.dto.RecruitmentSettingsUpdateRequest;
import com.bd.homepage.domain.recruitment.entity.RecruitmentSettings;
import com.bd.homepage.domain.recruitment.repository.RecruitmentSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentService {

    private static final Long FIXED_ID = 1L;

    private final RecruitmentSettingsRepository repository;

    @Transactional(readOnly = true)
    public RecruitmentSettingsResponse get() {
        RecruitmentSettings settings = repository.findById(FIXED_ID)
                .orElseGet(this::createDefault);

        return RecruitmentSettingsResponse.from(settings);
    }

    public void update(RecruitmentSettingsUpdateRequest req) {
        RecruitmentSettings settings = repository.findById(FIXED_ID)
                .orElseGet(this::createDefault);

        settings.update(
                req.getApplyLink(),
                req.getBannerTitle(),
                req.getBannerDesc(),
                req.getBannerImage()
        );
    }

    private RecruitmentSettings createDefault() {
        return repository.save(
                RecruitmentSettings.builder()
                        .id(FIXED_ID)
                        .bannerTitle("모집중")
                        .updatedAt(LocalDateTime.now())
                        .build()
        );
    }
}
