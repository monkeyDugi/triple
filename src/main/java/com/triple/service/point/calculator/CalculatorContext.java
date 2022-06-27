package com.triple.service.point.calculator;

import com.triple.domain.ActionType;
import com.triple.repository.PointHistoryRepository;
import com.triple.web.dto.PointSaveRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 리뷰 작성, 추가, 삭제 시 포인트 적립 컨텍스트
 */
@Service
public class CalculatorContext {
    private final PointHistoryRepository pointHistoryRepository;

    public CalculatorContext(PointHistoryRepository pointHistoryRepository) {
        this.pointHistoryRepository = pointHistoryRepository;
    }

    public int calculate(PointSaveRequest pointSaveRequest, UUID userId, UUID reviewId, UUID placeId) {
        if (pointSaveRequest.getAction() == ActionType.ADD) {
            return new AdditionCalculator(pointHistoryRepository)
                    .calculate(pointSaveRequest.getPhotoCount(), userId, null, placeId);
        }
        if (pointSaveRequest.getAction() == ActionType.MOD) {
            return new ModificationCalculator(pointHistoryRepository)
                    .calculate(pointSaveRequest.getPhotoCount(), userId, reviewId, null);
        }
        if (pointSaveRequest.getAction() == ActionType.DELETE) {
            return new DeletedCalculator(pointHistoryRepository)
                    .calculate(0, userId, reviewId, null);
        }
        return 0;
    }
}
