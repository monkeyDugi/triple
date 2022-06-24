package com.triple.service.point.calculator;

import com.triple.domain.ActionType;
import com.triple.domain.Place;
import com.triple.domain.PointHistory;
import com.triple.domain.Review;
import com.triple.repository.PointHistoryRepository;
import com.triple.web.dto.PointRequest;
import org.springframework.stereotype.Service;

@Service
public class CalculatorContext {
    private final PointHistoryRepository pointHistoryRepository;

    public CalculatorContext(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public int calculate(PointRequest pointRequest, Review review, Place place) {
        if (pointRequest.getAction() == ActionType.ADD) {
            return new AdditionCalculator()
                    .calculate(pointRequest.getPhotoCount(), 0, isNotFirstReview(place));
        }
        if (pointRequest.getAction() == ActionType.MOD) {
            return new ModificationCalculator()
                    .calculate(pointRequest.getPhotoCount(), findPrePhotoCount(review), true);
        }
        return 0;
    }

    private int findPrePhotoCount(Review review) {
        return pointHistoryRepository.findByReviewAndLatest(review)
                .orElse(PointHistory.emptyPointHistory())
                .getPhotoCount();
    }

    private boolean isNotFirstReview(Place place) {
        return pointHistoryRepository.findByPlaceAndLatest(place)
                .isPresent();
    }
}
