package com.triple.repository;

import com.triple.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {
}
