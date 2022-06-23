package com.triple.domain;

import com.triple.web.dto.PointRequest;

import java.util.function.Function;

/**
 * 리뷰 생성 이벤트 타입
 */
public enum ActionType {
    ADD("리뷰 생성", photoCount -> photoCount > 0 ? 2 : 1),
    MOD("리뷰 수정", null),
    DELETE("리뷰 삭제", null);

    private final String description;
    private final Function<Integer, Integer> function;

    ActionType(String dscription, Function<Integer, Integer> function) {
        this.description = dscription;
        this.function = function;
    }

    public int getScore(int photoCount) {
        return function.apply(photoCount);
    }

    public String getDescription() {
        return description;
    }
}
