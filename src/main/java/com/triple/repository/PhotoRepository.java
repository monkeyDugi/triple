package com.triple.repository;

import com.triple.domain.Photo;
import com.triple.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<Photo, UUID> {
    List<Photo> findAllByReview(Review review);

    @Transactional
    void deleteByReview(Review review);
}
