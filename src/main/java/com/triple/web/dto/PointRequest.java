package com.triple.web.dto;

import com.triple.domain.ActionType;
import com.triple.domain.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PointRequest {
    private EventType type;
    private ActionType action;
    private String content = "꼭 가야하는 곳이에요.";
    private List<UUID> attachedPhotoIds = new ArrayList<>();
    private UUID reviewId;
    private UUID userId;
    private UUID placeId;

    PointRequest() {

    }

    public PointRequest(EventType type, ActionType action, String content,
                        List<UUID> attachedPhotoIds, UUID reviewId, UUID userId, UUID placeId) {
        this.type = type;
        this.action = action;
        if (content != null) {
            this.content = content;
        }
        if (attachedPhotoIds != null) {
            this.attachedPhotoIds = attachedPhotoIds;
        }
        this.reviewId = reviewId;
        this.userId = userId;
        this.placeId = placeId;
    }

    public EventType getType() {
        return type;
    }

    public ActionType getAction() {
        return action;
    }

    public String getContent() {
        return content;
    }

    public List<UUID> getAttachedPhotoIds() {
        return Collections.unmodifiableList(attachedPhotoIds);
    }

    public int getPhotoCount() {
        return attachedPhotoIds.size();
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
