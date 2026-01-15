package com.bd.homepage.domain.winner.controller;

import com.bd.homepage.domain.winner.dto.WinnerDtos;
import com.bd.homepage.domain.winner.service.WinnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/winners")
public class WinnerAdminController {

    private final WinnerService winnerService;

    // 전체 조회
    @GetMapping
    public WinnerDtos.WinnersResponse getWinners() {
        return winnerService.getWinners();
    }

    // 공개 여부 / 안내문 수정
    @PutMapping("/settings")
    public ResponseEntity<Void> updateSettings(@RequestBody WinnerDtos.WinnersSettingsUpdateRequest req) {
        winnerService.updateSettings(req);
        return ResponseEntity.noContent().build();
    }

    // 합격자 추가
    @PostMapping("/names")
    public WinnerDtos.WinnerNameResponse createName(@RequestBody WinnerDtos.WinnerNameCreateRequest req) {
        return winnerService.createName(req);
    }

    // 합격자 수정
    @PutMapping("/names/{id}")
    public WinnerDtos.WinnerNameResponse updateName(
            @PathVariable UUID id,
            @RequestBody WinnerDtos.WinnerNameUpdateRequest req
    ) {
        return winnerService.updateName(id, req);
    }

    // 합격자 삭제
    @DeleteMapping("/names/{id}")
    public ResponseEntity<Void> deleteName(@PathVariable UUID id) {
        winnerService.deleteName(id);
        return ResponseEntity.noContent().build();
    }
}
