package com.bd.homepage.domain.winner.repository;

import com.bd.homepage.domain.winner.entity.WinnerName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WinnerNameRepository extends JpaRepository<WinnerName, UUID> {
    List<WinnerName> findAllByOrderByOrderIndexAscCreatedAtAsc();
}
