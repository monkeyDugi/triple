package com.triple.acceptance;

import com.triple.domain.ActionType;
import com.triple.domain.EventType;
import com.triple.domain.Place;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PlaceRepository;
import com.triple.repository.ReviewRepository;
import com.triple.repository.UserRepository;
import com.triple.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.triple.acceptance.PointStepsAssert.포인트_적립됨;
import static com.triple.acceptance.PointStepsRequest.포인트_적립_요청;
import static com.triple.util.CommonUtils.DEFAULT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.DEFAULT_CONTENT;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("포인트 관리")
public class PointAcceptanceTest extends AcceptanceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    /**
     * Given 리뷰 내용과 사진 첨부 리뷰 작성됨
     * When 포인트 적립 요청
     * Then 포인트 적립됨
     */
    @Test
    void 첫_리뷰_내용과_사진_첨부_리뷰_생성_이벤트_포인트_적립() {
        User user = 회원_생성됨();
        Place place = 장소_생성(user);
        Review review = 리뷰_생성됨(user, place);

        List<UUID> attachedPhotoIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        ExtractableResponse<Response> response = 포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), user.getId(), place.getId());

        포인트_적립됨(response);
    }

    private User 회원_생성됨() {
        return userRepository.save(new User(DEFAULT_ACCOUNT_ID));
    }

    private Place 장소_생성(User user) {
        return placeRepository.save(new Place(user));
    }

    private Review 리뷰_생성됨(User user, Place place) {
        return reviewRepository.save(new Review(user, place));
    }
}
