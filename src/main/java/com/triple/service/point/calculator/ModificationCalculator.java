package com.triple.service.point.calculator;

/**
 * 리뷰 수정 시 적립될 포인트를 반환한다.
 */
public class ModificationCalculator implements ActionCalculator {
    @Override
    public int calculate(int currentPhotoCount, int prePhotoCount, boolean isNotFirstReview, int score) {
        if (isDeletedAllPhoto(currentPhotoCount, prePhotoCount)) {
            return -1;
        } else if (isCreatedPhoto(currentPhotoCount, prePhotoCount)) {
            return 1;
        }
        return 0;
    }

    private boolean isDeletedAllPhoto(int currentPhotoCount, int prePhotoCount) {
        return currentPhotoCount < 1 && currentPhotoCount < prePhotoCount;
    }

    private boolean isCreatedPhoto(int currentPhotoCount, int prePhotoCount) {
        return prePhotoCount == 0 && currentPhotoCount > prePhotoCount;
    }
}
