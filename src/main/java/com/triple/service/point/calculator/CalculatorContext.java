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
        PointHistory prePointHistory = findPrePointHistory(review);

        if (pointRequest.getAction() == ActionType.ADD) {
            return new AdditionCalculator()
                    .calculate(pointRequest.getPhotoCount(), 0, isNotFirstReview(place), 0);
        }
        if (pointRequest.getAction() == ActionType.MOD) {
            return new ModificationCalculator()
                    .calculate(pointRequest.getPhotoCount(), prePointHistory.getPhotoCount(), true, 0);
        }
        if (pointRequest.getAction() == ActionType.DELETE) {
            return new DeletedCalculator()
                    .calculate(0, 0, true, prePointHistory.getScore());
        }
        return 0;
    }

    private PointHistory findPrePointHistory(Review review) {
        return pointHistoryRepository.findByReviewAndLatest(review)
                .orElse(PointHistory.emptyPointHistory());
    }

    private boolean isNotFirstReview(Place place) {
        return pointHistoryRepository.findByPlaceAndLatest(place)
                .isPresent();
    }
}
