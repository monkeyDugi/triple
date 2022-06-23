package com.triple.domain;

import java.util.function.Function;

/**
 * 리뷰 생성 이벤트 타입
 */
public enum ActionType {
    ADD("리뷰 생성", photoCount -> {
        if (photoCount > 0) {
            return 2;
        }
        return 1;
    }, isNotFirstReview -> {
        if (isNotFirstReview) {
            return 0;
        }
        return 1;
    }),
    MOD("리뷰 수정", null, null),
    DELETE("리뷰 삭제", null, null);

    private final String description;
    private final Function<Integer, Integer> contentScoreFunction;
    private final Function<Boolean, Integer> bonusScoreFunction;

    ActionType(String dscription, Function<Integer, Integer> contentScoreFunction, Function<Boolean, Integer> bonusScoreFunction) {
        this.description = dscription;
        this.contentScoreFunction = contentScoreFunction;
        this.bonusScoreFunction = bonusScoreFunction;
    }

    public int getScore(Integer photoCount) {
        return contentScoreFunction.apply(photoCount);
    }

    public int getBonusScore(boolean isFirstReview) {
        return bonusScoreFunction.apply(isFirstReview);
    }

    public String getDescription() {
        return description;
    }
}
