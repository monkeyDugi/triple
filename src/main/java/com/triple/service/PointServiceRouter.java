package com.triple.service;

import com.triple.domain.Point;
import com.triple.exception.BusinessException;
import com.triple.repository.PointRepository;
import com.triple.web.dto.PointResponse;
import com.triple.web.dto.PointSaveRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.triple.exception.ExceptionCode.NOT_SUPPORTED_TYPE;

@Service
public class PointServiceRouter {
    private final List<PointService> pointServices;
    private final PointRepository pointRepository;

    public PointServiceRouter(List<PointService> pointServices, PointRepository pointRepository) {
        this.pointServices = pointServices;
        this.pointRepository = pointRepository;
    }

    public void actionPoint(PointSaveRequest pointSaveRequest) {
        getPointService(pointSaveRequest).actionPoint(pointSaveRequest);
    }

    public PointResponse findPoint(UUID userId) {
        Point point = pointRepository.findByUserId(userId)
                .orElse(Point.emptyPoint(userId));
        return new PointResponse(point.getId(), point.getScore(), point.getUser().getId());
    }

    private PointService getPointService(PointSaveRequest pointSaveRequest) {
        return pointServices.stream()
                .filter(pointService -> pointService.isSupportedType(pointSaveRequest.getType()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(NOT_SUPPORTED_TYPE));
    }
}
