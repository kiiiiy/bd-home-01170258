package com.bd.homepage.domain.recruitment.controller;

import com.bd.homepage.domain.recruitment.dto.RecruitmentSettingsResponse;
import com.bd.homepage.domain.recruitment.dto.RecruitmentSettingsUpdateRequest;
import com.bd.homepage.domain.recruitment.service.RecruitmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/recruitment")
@RequiredArgsConstructor
public class RecruitmentAdminController {

    private final RecruitmentService recruitmentService;

    @GetMapping
    public RecruitmentSettingsResponse get() {
        return recruitmentService.get();
    }

    @PutMapping
    public void update(
            @RequestBody @Valid RecruitmentSettingsUpdateRequest req
    ) {
        recruitmentService.update(req);
    }
}
