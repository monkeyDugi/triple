package com.triple.service;

import com.triple.domain.Place;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public Review findById(UUID id, User user, Place place) {
        return reviewRepository.findById(id)
                .orElse(Review.emptyReview(id, user, place));
    }
}
