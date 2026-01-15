package com.bd.homepage.domain.winner.service;

import com.bd.homepage.domain.winner.dto.WinnerDtos;
import com.bd.homepage.domain.winner.entity.WinnerName;
import com.bd.homepage.domain.winner.entity.WinnersSettings;
import com.bd.homepage.domain.winner.repository.WinnerNameRepository;
import com.bd.homepage.domain.winner.repository.WinnersSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Transactional
public class WinnerService {

    private final WinnersSettingsRepository settingsRepository;
    private final WinnerNameRepository winnerNameRepository;

    @Transactional(readOnly = true)
    public WinnerDtos.WinnersResponse getWinners() {
        WinnersSettings settings = getOrCreateSettingsRow();
        List<WinnerName> names = winnerNameRepository.findAllByOrderByOrderIndexAscCreatedAtAsc();
        return WinnerDtos.WinnersResponse.of(settings, names);
    }

    public void updateSettings(WinnerDtos.WinnersSettingsUpdateRequest req) {
        WinnersSettings settings = getOrCreateSettingsRow();
        settings.setPublic(req.isPublic());
        settings.setNotice(req.getNotice());
        settingsRepository.save(settings);
    }

    public WinnerDtos.WinnerNameResponse createName(WinnerDtos.WinnerNameCreateRequest req) {
        validateName(req.getName());

        WinnerName saved = winnerNameRepository.save(
                WinnerName.builder()
                        .name(req.getName().trim())
                        .orderIndex(req.getOrderIndex())
                        .build()
        );
        return WinnerDtos.WinnerNameResponse.from(saved);
    }

    public WinnerDtos.WinnerNameResponse updateName(
            UUID id,
            WinnerDtos.WinnerNameUpdateRequest req
    ) {
        validateName(req.getName());

        WinnerName e = winnerNameRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        id + "번 합격자를 찾을 수 없습니다."
                ));

        e.setName(req.getName().trim());
        e.setOrderIndex(req.getOrderIndex());
        return WinnerDtos.WinnerNameResponse.from(e);
    }


    public void deleteName(UUID id) {
        if (!winnerNameRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    id + "번 합격자를 찾을 수 없습니다."
            );
        }
        winnerNameRepository.deleteById(id);
    }

    private WinnersSettings getOrCreateSettingsRow() {
        return settingsRepository.findById(1L).orElseGet(() -> settingsRepository.save(WinnersSettings.defaultRow()));
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ResponseStatusException(BAD_REQUEST, "이름은 필수입니다.");
        }
        if (name.trim().length() > 100) {
            throw new ResponseStatusException(BAD_REQUEST, "이름의 길이는 100자 이하여야 합니다.");
        }
    }
}
