package com.triple.service.point.calculator;

import com.triple.domain.PointHistory;
import com.triple.repository.PointHistoryRepository;

import java.util.UUID;

/**
 * 리뷰 삭제 시 차감될 포인트를 반환한다.
 */
public class DeletedCalculator implements ActionCalculator {
    private final PointHistoryRepository pointHistoryRepository;

    public DeletedCalculator(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public int calculate(int unused1, UUID reviewId, UUID unused2) {
        return - findPreScore(reviewId);
    }

    private int findPreScore(UUID reviewId) {
        return pointHistoryRepository.findByReviewIdAndLatest(reviewId)
                .orElse(PointHistory.emptyPointHistory())
                .getScore();
    }
}
