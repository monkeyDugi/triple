package com.triple.service.point.calculator;

public interface ActionCalculator {
    int calculate(int currentPhotoCount, int prePhotoCount, boolean isNotFirstReview, int score);
}
