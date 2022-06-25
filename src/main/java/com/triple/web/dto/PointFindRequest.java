package com.triple.web.dto;

import java.util.UUID;

public class PointFindRequest {
    private UUID userId;

    PointFindRequest() {

    }

    public PointFindRequest(UUID userId) {
        this.userId = userId;
    }

    public UUID getUserId() {
        return userId;
    }
}
