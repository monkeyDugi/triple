package com.triple.service.point.calculator;

/**
 * 리뷰 추가 시 적립될 포인트를 반환한다.
 */
public class AdditionCalculator implements ActionCalculator {
    @Override
    public int calculate(int currentPhotoCount, int unusedParameter, boolean isNotFirstReview) {
        int contentScore = currentPhotoCount > 0 ? 2 : 1;
        int bonusScore = isNotFirstReview ? 0 : 1;

        return contentScore + bonusScore;
    }
}
