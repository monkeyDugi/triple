package com.triple.web.dto;

import com.triple.domain.ActionType;
import com.triple.domain.EventType;

import java.util.List;
import java.util.UUID;

public class PointRequest {
    private EventType eventType;
    private ActionType actionType;
    private String content;
    private List<UUID> attachedPhotoIds;
    private UUID reviewId;
    private UUID userId;
    private UUID placeId;

    PointRequest() {

    }

    public EventType getEventType() {
        return eventType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public String getContent() {
        return content;
    }

    public List<UUID> getAttachedPhotoIds() {
        return attachedPhotoIds;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getPlaceId() {
        return placeId;
    }
}
