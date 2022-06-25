package com.triple.service.point.calculator;

import com.triple.domain.ActionType;
import com.triple.repository.PointHistoryRepository;
import com.triple.web.dto.PointRequest;
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

    public int calculate(PointRequest pointRequest, UUID reviewId, UUID placeId) {
        if (pointRequest.getAction() == ActionType.ADD) {
            return new AdditionCalculator(pointHistoryRepository)
                    .calculate(pointRequest.getPhotoCount(), null, placeId);
        }
        if (pointRequest.getAction() == ActionType.MOD) {
            return new ModificationCalculator(pointHistoryRepository)
                    .calculate(pointRequest.getPhotoCount(), reviewId, null);
        }
        if (pointRequest.getAction() == ActionType.DELETE) {
            return new DeletedCalculator(pointHistoryRepository)
                    .calculate(0, reviewId, null);
        }
        return 0;
    }
}
