package com.triple.service;

public class AddCalculator implements ActionCalculator {
    @Override
    public int calculate(int currentPhotoCount, int prePhotoCount, boolean isNotFirstReview) {
        int contentScore = currentPhotoCount > 0 ? 2 : 1;
        int bonusScore = isNotFirstReview ? 0 : 1;

        return contentScore + bonusScore;
    }
}
