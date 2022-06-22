package com.triple.acceptance;

import com.triple.domain.ActionType;
import com.triple.domain.EventType;
import com.triple.domain.User;
import com.triple.domain.Place;
import com.triple.domain.Review;
import com.triple.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.triple.util.CommonUtils.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

@DisplayName("포인트 관리")
public class PointAcceptanceTest extends AcceptanceTest {
    private final User user = new User(UUID.randomUUID(), DEFAULT_ACCOUNT_ID);
    private final Place place = new Place(UUID.randomUUID(), user);

    /**
     * Given 리뷰 생성됨
     * When 포인트 적립 요청
     * Then 포인트 적립됨
     */
    @Test
    void 리뷰_생성_이벤트_포인트_적립() {
        Review review = 리뷰_생성됨();

        List<UUID> attachedPhotoIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        ExtractableResponse<Response> response = 포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), user.getId(), place.getId());

        포인트_적립됨(response);
    }

    private Review 리뷰_생성됨() {
        return new Review(UUID.randomUUID(), user, place);
    }

    private ExtractableResponse<Response> 포인트_적립_요청(RequestSpecification given, ActionType actionType,
                                                    List<UUID> attachedPhotoIds, UUID reviewId, UUID userId, UUID placeId) {

        Map<String, Object> params = new HashMap<>();
        params.put("type", EventType.REVIEW);
        params.put("action", actionType.ADD);
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

    private void 포인트_적립됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
