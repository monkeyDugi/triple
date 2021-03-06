package com.triple.service.review.calculator;

import java.util.UUID;

public interface ActionCalculator {
    /**
     *
     * @param photoCount: 리뷰의 사진 개수
     * @param reviewId: 리뷰 아이디
     * @param placeId: 리뷰 장소
     * @return 적립 및 차감될 포인트
     */
    int calculate(int photoCount, UUID userId, UUID reviewId, UUID placeId);
}
