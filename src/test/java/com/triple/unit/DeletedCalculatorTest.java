package com.triple.unit;

import com.triple.service.point.calculator.ActionCalculator;
import com.triple.service.point.calculator.DeletedCalculator;
import com.triple.service.point.calculator.ModificationCalculator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DeletedCalculatorTest {
    @Test
    void 리뷰_삭제_점수() {
        // given
        ActionCalculator calculator = new DeletedCalculator();

        int currentPhotoCount = 1;
        int prePhotoCount = 0;
        boolean isNotFirstReview = false;
        int currentScore = 3;

        // when
        int score = calculator.calculate(currentPhotoCount, prePhotoCount, isNotFirstReview, currentScore);

        // when
        assertThat(score).isEqualTo(-3);
    }
}
