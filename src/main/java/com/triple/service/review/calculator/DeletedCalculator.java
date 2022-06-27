package com.triple.service.review.calculator;

import com.triple.exception.BusinessException;
import com.triple.exception.ExceptionCode;
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

    public int calculate(int unused1, UUID userId, UUID reviewId, UUID unused2) {
        return - findPreScore(userId, reviewId);
    }

    private int findPreScore(UUID userId, UUID reviewId) {
        return pointHistoryRepository.findByUserIdAndReviewIdLatest(userId, reviewId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.MEMBER_AUTHORIZATION))
                .getScore();
    }
}
