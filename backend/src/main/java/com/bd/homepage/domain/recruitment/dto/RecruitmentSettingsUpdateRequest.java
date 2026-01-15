package com.bd.homepage.domain.recruitment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RecruitmentSettingsUpdateRequest {

    private String applyLink;

    @NotBlank
    private String bannerTitle;

    private String bannerDesc;
    private String bannerImage;
}
