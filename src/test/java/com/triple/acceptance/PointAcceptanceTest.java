package com.triple.acceptance;

import com.triple.domain.ActionType;
import com.triple.domain.Photo;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.triple.acceptance.PointStepsAssert.*;
import static com.triple.acceptance.PointStepsAssert.포인트_적립됨;
import static com.triple.acceptance.PointStepsAssert.포인트_조회됨;
import static com.triple.acceptance.PointStepsRequest.포인트_적립_요청;
import static com.triple.acceptance.PointStepsRequest.포인트_조회_요청;
import static com.triple.util.CommonUtils.ADDRESS1;
import static com.triple.util.CommonUtils.ORIGIN_FILE_NAME;
import static com.triple.util.CommonUtils.PLACE_NAME;
import static com.triple.util.CommonUtils.PLACE_REGISTRANT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.REVIEWER_ACCOUNT_ID1;
import static com.triple.util.CommonUtils.REVIEWER_ACCOUNT_ID2;
import static com.triple.util.CommonUtils.STORE_FILE_NAME1;
import static com.triple.util.CommonUtils.STORE_FILE_NAME2;
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
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);

        ExtractableResponse<Response> response = 포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        포인트_적립됨(response);
    }

    /**
     * Given 첫 리뷰 내용과 사진 첨부 리뷰 작성됨
     * And 포인트 적립됨
     * And 리뷰 수정됨(이미지 삭제)
     * When 포인트 적립 요청
     * Then 포인트 적립됨
     */
    @Test
    void 첫_리뷰_수정_이벤트_포인트_적립() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);
        포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());
        List<UUID> deleteAttachedPhotoIds = 리뷰_이미지_삭제됨(review.getId());

        ExtractableResponse<Response> response =
                포인트_적립_요청(given(), ActionType.MOD, deleteAttachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        포인트_적립됨(response);
    }

    /**
     * Given 첫 리뷰 내용과 사진 첨부 리뷰 작성됨
     * And 포인트 적립됨
     * And 리뷰 삭제
     * When 포인트 적립 요청
     * Then 포인트 차감됨
     */
    @Test
    void 첫_리뷰_삭제_이벤트_포인트_적립() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);
        포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());
        리뷰_삭제됨(review);

        ExtractableResponse<Response> response =
                포인트_적립_요청(given(), ActionType.DELETE, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        포인트_적립됨(response);
    }

    /**
     * Given 리뷰 내용과 사진 첨부 리뷰 작성됨
     * And 포인트 적립 요청
     * When 포인트 조회 요청
     * Then 포인트 조회됨
     */
    @Test
    void 첫_리뷰_내용과_사진_첨부_리뷰_생성_이벤트_포인트_조회() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);
        포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        ExtractableResponse<Response> response = 포인트_조회_요청(given(), userReviewer.getId());

        포인트_조회됨(response, 3);
    }

    /**
     * Given 포인트 적립 내역없음
     * When 포인트 조회 요청
     * Then 포인트 조회됨
     */
    @Test
    void 포인트_없는_경우_포인트_조회() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);

        ExtractableResponse<Response> response = 포인트_조회_요청(given(), userReviewer.getId());

        포인트_조회됨(response, 0);
    }

    /**
     * When 포인트 적립 요청
     * Then 포인트 적립안됨
     */
    @Test
    void 사용자가_존재하지_않는_경우() {
        List<UUID> attachedPhotoIds = new ArrayList<>();
        ExtractableResponse<Response> response = 포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID());

        포인트_적립안됨(response);
    }

    /**
     * Given 첫 리뷰 내용과 사진 첨부 리뷰 작성됨
     * And 포인트 적립됨
     * And 다른 사용자 회원가입됨
     * When 다른 사용자가 가짜 데이터로 리뷰 수정 이벤트로 포인트 적립 요청
     * Then 포인트 적립안됨
     */
    @DisplayName("장소와 리뷰 아이디를 알고 본인 아이디로 포인트 차감 및 적립 요청 시 진짜 사용자의 포인트가 차감되지 않도록 예외 처리한다.")
    @Test
    void 수정된_리뷰_권한_체크() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);
        포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());
        List<UUID> deleteAttachedPhotoIds = 리뷰_이미지_삭제됨(review.getId());
        User otherMember = 회원_생성됨(REVIEWER_ACCOUNT_ID2);

        ExtractableResponse<Response> response =
                포인트_적립_요청(given(), ActionType.MOD, deleteAttachedPhotoIds, review.getId(), otherMember.getId(), place.getId());

        권한_없음(response);
    }

    /**
     * Given 첫 리뷰 내용과 사진 첨부 리뷰 작성됨
     * And 포인트 적립됨
     * And 다른 사용자 회원가입됨
     * When 다른 사용자가 가짜 데이터로 리뷰 삭제 이벤트로 포인트 적립 요청
     * Then 포인트 적립안됨
     */
    @DisplayName("장소와 리뷰 아이디를 알고 본인 아이디로 포인트 차감 요청 시 진짜 사용자의 포인트가 차감되지 않도록 예외 처리한다.")
    @Test
    void 삭제된_리뷰_권한_체크() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);
        포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());
        List<UUID> deleteAttachedPhotoIds = 리뷰_이미지_삭제됨(review.getId());
        User otherMember = 회원_생성됨(REVIEWER_ACCOUNT_ID2);

        ExtractableResponse<Response> response =
                포인트_적립_요청(given(), ActionType.DELETE, deleteAttachedPhotoIds, review.getId(), otherMember.getId(), place.getId());

        권한_없음(response);
    }

    private User 회원_생성됨(String accountId) {
        return userRepository.save(new User(accountId));
    }

    private Place 장소_생성(User user) {
        return placeRepository.save(new Place(ADDRESS1, PLACE_NAME, user));
    }

    private Review 리뷰_생성됨(User user, Place place) {
        return reviewRepository.save(new Review(user, place));
    }

    private void 리뷰_삭제됨(Review review) {
        ReflectionTestUtils.setField(review, "deleted", true);
        reviewRepository.save(review);
    }

    private List<UUID> 리뷰_이미지_생성됨(Review review) {
        List<Photo> photos = Arrays.asList(
                new Photo(ORIGIN_FILE_NAME, STORE_FILE_NAME1, review),
                new Photo(ORIGIN_FILE_NAME, STORE_FILE_NAME2, review)
        );
        return photos.stream()
                .map(Photo::getId)
                .collect(Collectors.toList());
    }

    private List<UUID> 리뷰_이미지_삭제됨(UUID reviewId) {
        return new ArrayList<>();
    }
}
