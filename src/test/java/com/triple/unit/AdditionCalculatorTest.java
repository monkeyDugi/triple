package com.triple.unit;

import com.triple.service.point.calculator.ActionCalculator;
import com.triple.service.point.calculator.AdditionCalculator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AdditionCalculatorTest {
    @Test
    void 첫_리뷰_내용과_사진_첨부_리뷰_점수() {
        // given
        ActionCalculator calculator = new AdditionCalculator();

        int currentPhotoCount = 1;
        int prePhotoCount = 0;
        boolean isNotFirstReview = false;
        int currentScore = 0;

        // when
        int score = calculator.calculate(currentPhotoCount, prePhotoCount, isNotFirstReview, currentScore);

        // when
        assertThat(score).isEqualTo(3);
    }

    @Test
    void 첫_리뷰_내용_리뷰_점수() {
        // given
        ActionCalculator calculator = new AdditionCalculator();

        int currentPhotoCount = 0;
        int prePhotoCount = 0;
        boolean isNotFirstReview = false;
        int currentScore = 0;

        // when
        int score = calculator.calculate(currentPhotoCount, prePhotoCount, isNotFirstReview, currentScore);

        // when
        assertThat(score).isEqualTo(2);
    }

    @Test
    void 리뷰_내용과_사진_첨부_리뷰_점수() {
        // given
        ActionCalculator calculator = new AdditionCalculator();

        int currentPhotoCount = 1;
        int prePhotoCount = 0;
        boolean isNotFirstReview = true;
        int currentScore = 0;

        // when
        int score = calculator.calculate(currentPhotoCount, prePhotoCount, isNotFirstReview, currentScore);

        // when
        assertThat(score).isEqualTo(2);
    }

    @Test
    void 리뷰_내용_리뷰_점수() {
        // given
        ActionCalculator calculator = new AdditionCalculator();

        int currentPhotoCount = 0;
        int prePhotoCount = 0;
        boolean isNotFirstReview = true;
        int currentScore = 0;

        // when
        int score = calculator.calculate(currentPhotoCount, prePhotoCount, isNotFirstReview, currentScore);

        // when
        assertThat(score).isEqualTo(1);
    }
}
