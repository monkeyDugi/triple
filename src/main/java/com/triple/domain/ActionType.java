package com.triple.domain;

/**
 * 리뷰 생성 이벤트 타입
 */
public enum ActionType {
    ADD("리뷰 생성"),
    MOD("리뷰 수정"),
    DELETE("리뷰 삭제");

    private final String description;

    ActionType(String dscription) {
        this.description = dscription;
    }

    public String getDescription() {
        return description;
    }
}
