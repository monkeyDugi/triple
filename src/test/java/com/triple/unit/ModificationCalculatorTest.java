package com.triple.unit;

import com.triple.service.point.calculator.ActionCalculator;
import com.triple.service.point.calculator.ModificationCalculator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ModificationCalculatorTest {
    @Test
    void 사진_추가_리뷰_점수() {
        // given
        ActionCalculator calculator = new ModificationCalculator();

        int currentPhotoCount = 1;
        int prePhotoCount = 0;
        boolean unusedParameter = false;

        // when
        int score = calculator.calculate(currentPhotoCount, prePhotoCount, unusedParameter);

        // when
        assertThat(score).isEqualTo(1);
    }

    @Test
    void 사진_모두_삭제_리뷰_점수() {
        // given
        ActionCalculator calculator = new ModificationCalculator();

        int currentPhotoCount = 0;
        int prePhotoCount = 1;
        boolean unusedParameter = false;

        // when
        int score = calculator.calculate(currentPhotoCount, prePhotoCount, unusedParameter);

        // when
        assertThat(score).isEqualTo(-1);
    }

    @Test
    void 사진_한_개_남기고_삭제_리뷰_점수() {
        // given
        ActionCalculator calculator = new ModificationCalculator();

        int currentPhotoCount = 1;
        int prePhotoCount = 2;
        boolean unusedParameter = false;

        // when
        int score = calculator.calculate(currentPhotoCount, prePhotoCount, unusedParameter);

        // when
        assertThat(score).isEqualTo(0);
    }
}
