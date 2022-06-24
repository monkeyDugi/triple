package com.triple.service.point.calculator;

/**
 * 리뷰 추가 시 적립될 포인트를 반환한다.
 */
public class DeletedCalculator implements ActionCalculator {
    @Override
    public int calculate(int currentPhotoCount, int prePhotoCount, boolean isNotFirstReview, int score) {
        return -score;
    }
}
