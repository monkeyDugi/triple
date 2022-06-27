package com.triple.service;

import com.triple.domain.EventType;
import com.triple.web.dto.PointSaveRequest;

public interface PointService {
    /**
     * 이벤트 타입 지원 여부 체크
     */
    boolean isSupportedType(EventType eventType);

    /**
     * 포인트 적립
     */
    void actionPoint(PointSaveRequest pointSaveRequest);
}
