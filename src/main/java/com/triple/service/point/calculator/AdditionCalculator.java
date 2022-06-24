package com.triple.service.point.calculator;

/**
 * 리뷰 추가 시 적립될 포인트를 반환한다.
 */
public class AdditionCalculator implements ActionCalculator {
    @Override
    public int calculate(int currentPhotoCount, int prePhotoCount, boolean isNotFirstReview, int score) {
        int contentScore = calculateContentScore(currentPhotoCount);
        int bonusScore = calculateBonusScore(isNotFirstReview);

        return contentScore + bonusScore;
    }

    private int calculateBonusScore(boolean isNotFirstReview) {
        if (isNotFirstReview){
            return 0;
        }
        return 1;
    }

    private int calculateContentScore(int currentPhotoCount) {
        if (currentPhotoCount > 0){
            return 2;
        }
        return 1;
    }
}
