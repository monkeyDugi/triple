package com.triple.acceptance;

import com.triple.domain.ActionType;
import com.triple.domain.EventType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.triple.util.CommonUtils.DEFAULT_CONTENT;

public class PointStepsRequest {
    public static ExtractableResponse<Response> 포인트_적립_요청(RequestSpecification given, ActionType actionType,
                                                    List<UUID> attachedPhotoIds, UUID reviewId, UUID userId, UUID placeId) {

        Map<String, Object> params = new HashMap<>();
        params.put("type", EventType.REVIEW);
        params.put("action", actionType);
        params.put("content", DEFAULT_CONTENT);
        params.put("attachedPhotoIds", attachedPhotoIds);
        params.put("reviewId", reviewId);
        params.put("userId", userId);
        params.put("placeId", placeId);

        return given.log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/events")
                .then().log().all()
                .extract();
    }
}
