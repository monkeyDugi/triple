package com.triple.repository;

import com.triple.domain.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    @Transactional
    void deleteByReviewId(UUID reviewId);
}
