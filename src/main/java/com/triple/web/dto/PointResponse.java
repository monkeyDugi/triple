package com.triple.web.dto;

import java.util.UUID;

public class PointResponse {
    private UUID id;
    private int score;
    private UUID userId;

    public PointResponse(UUID id, int score, UUID userId) {
        this.id = id;
        this.score = score;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public UUID getUserId() {
        return userId;
    }
}
