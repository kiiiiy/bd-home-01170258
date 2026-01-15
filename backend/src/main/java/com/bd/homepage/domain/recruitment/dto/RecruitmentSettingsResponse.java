package com.bd.homepage.domain.recruitment.dto;

import com.bd.homepage.domain.recruitment.entity.RecruitmentSettings;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecruitmentSettingsResponse {

    private String applyLink;
    private String bannerTitle;
    private String bannerDesc;
    private String bannerImage;

    public static RecruitmentSettingsResponse from(RecruitmentSettings entity) {
        return RecruitmentSettingsResponse.builder()
                .applyLink(entity.getApplyLink())
                .bannerTitle(entity.getBannerTitle())
                .bannerDesc(entity.getBannerDesc())
                .bannerImage(entity.getBannerImage())
                .build();
    }
}
