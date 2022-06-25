package com.triple.repository;

import com.triple.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PointRepository extends JpaRepository<Point, UUID> {
    Optional<Point> findByUserId(UUID userId);
}
