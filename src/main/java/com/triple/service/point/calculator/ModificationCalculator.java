package com.triple.service.point.calculator;

import com.triple.domain.PointHistory;
import com.triple.exception.BusinessException;
import com.triple.exception.ExceptionCode;
import com.triple.repository.PointHistoryRepository;

import java.util.UUID;

/**
 * 리뷰 수정 시 적립 및 차감될 포인트를 반환한다.
 */
public class ModificationCalculator implements ActionCalculator {
    private final PointHistoryRepository pointHistoryRepository;

    public ModificationCalculator(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    @Override
    public int calculate(int photoCount, UUID userId, UUID reviewId, UUID unused) {
        int prePhotoCount = findPrePhotoCount(userId, reviewId);

        if (isDeletedAllPhoto(photoCount, prePhotoCount)) {
            return -1;
        } else if (isCreatedPhoto(photoCount, prePhotoCount)) {
            return 1;
        }
        return 0;
    }

    private boolean isDeletedAllPhoto(int photoCount, int prePhotoCount) {
        return photoCount < 1 && photoCount < prePhotoCount;
    }

    private boolean isCreatedPhoto(int photoCount, int prePhotoCount) {
        return prePhotoCount == 0 && photoCount > prePhotoCount;
    }

    private int findPrePhotoCount(UUID userId, UUID reviewId) {
        return pointHistoryRepository.findByUserIdAndReviewIdLatest(userId, reviewId)
                .orElseThrow(() -> new BusinessException(ExceptionCode.MEMBER_AUTHORIZATION))
                .getPhotoCount();
    }
}
