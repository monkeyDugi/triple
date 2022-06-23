package com.triple.acceptance;

import com.triple.domain.ActionType;
import com.triple.domain.Place;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PlaceRepository;
import com.triple.repository.ReviewRepository;
import com.triple.repository.UserRepository;
import com.triple.util.AcceptanceTest;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.triple.acceptance.PointStepsAssert.포인트_적립됨;
import static com.triple.acceptance.PointStepsRequest.포인트_적립_요청;
import static com.triple.util.CommonUtils.FIRST_REVIEWER_ACCOUNT_ID;
import static com.triple.util.CommonUtils.PLACE_REGISTRANT_ACCOUNT_ID;
import static io.restassured.RestAssured.given;

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
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);

        User userReviewer = 회원_생성됨(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = 리뷰_생성됨(userReviewer, place);

        List<UUID> attachedPhotoIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        ExtractableResponse<Response> response = 포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        포인트_적립됨(response);
    }

    private User 회원_생성됨(String accountId) {
        return userRepository.save(new User(accountId));
    }

    private Place 장소_생성(User user) {
        return placeRepository.save(new Place(user));
    }

    private Review 리뷰_생성됨(User user, Place place) {
        return reviewRepository.save(new Review(user, place));
    }
}
