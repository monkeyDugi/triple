package com.triple.repository;

import com.triple.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
//    Optional<Review> findByIdAndDeletedFalse(UUID id);
}
