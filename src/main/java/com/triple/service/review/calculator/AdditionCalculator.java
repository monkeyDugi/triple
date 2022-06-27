package com.triple.service.review.calculator;

import com.triple.domain.PointHistory;
import com.triple.repository.PointHistoryRepository;

import java.util.UUID;

/**
 * 리뷰 추가 시 적립될 포인트를 반환한다.
 */
public class AdditionCalculator implements ActionCalculator {
    private final PointHistoryRepository pointHistoryRepository;

    public AdditionCalculator(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Override
    public int calculate(int photoCount, UUID userId, UUID unused, UUID placeId) {
        int contentScore = calculateContentScore(photoCount);
        int bonusScore = calculateBonusScore(isFirstReview(placeId));

        return contentScore + bonusScore;
    }

    private int calculateContentScore(int photoCount) {
        if (photoCount > 0){
            return 2;
        }
        return 1;
    }

    private int calculateBonusScore(boolean isFirstReview) {
        if (isFirstReview){
            return 1;
        }
        return 0;
    }

    private boolean isFirstReview(UUID placeId) {
        return pointHistoryRepository.findByPlaceIdAndLatest(placeId)
                .orElse(PointHistory.emptyPointHistory())
                .isFirstReview();
    }
}
