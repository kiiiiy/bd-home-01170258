package com.bd.homepage.domain.winner.dto;

import com.bd.homepage.domain.winner.entity.WinnerName;
import com.bd.homepage.domain.winner.entity.WinnersSettings;
import lombok.*;

import java.util.List;
import java.util.UUID;

public class WinnerDtos {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class WinnersSettingsUpdateRequest {
        private boolean isPublic;
        private String notice;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class WinnerNameCreateRequest {
        private String name;
        private int orderIndex;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor
    public static class WinnerNameUpdateRequest {
        private String name;
        private int orderIndex;
    }

    @Getter @Builder
    public static class WinnerNameResponse {
        private UUID id;
        private String name;
        private int orderIndex;

        public static WinnerNameResponse from(WinnerName e) {
            return WinnerNameResponse.builder()
                    .id(e.getId())
                    .name(e.getName())
                    .orderIndex(e.getOrderIndex())
                    .build();
        }
    }

    @Getter @Builder
    public static class WinnersResponse {
        private boolean isPublic;
        private String notice;
        private List<WinnerNameResponse> names;

        public static WinnersResponse of(WinnersSettings s, List<WinnerName> names) {
            return WinnersResponse.builder()
                    .isPublic(s.isPublic())
                    .notice(s.getNotice())
                    .names(names.stream().map(WinnerNameResponse::from).toList())
                    .build();
        }
    }
}
