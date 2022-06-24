package com.triple.acceptance;

import com.triple.domain.ActionType;
import com.triple.domain.Photo;
import com.triple.domain.Place;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PhotoRepository;
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
import java.util.stream.Collectors;

import static com.triple.acceptance.PointStepsAssert.포인트_적립됨;
import static com.triple.acceptance.PointStepsRequest.포인트_적립_요청;
import static com.triple.util.CommonUtils.DEFAULT_ADDRESS;
import static com.triple.util.CommonUtils.DEFAULT_ORIGIN_FILE_NAME;
import static com.triple.util.CommonUtils.DEFAULT_PLACE_NAME;
import static com.triple.util.CommonUtils.FIRST_REVIEWER_ACCOUNT_ID;
import static com.triple.util.CommonUtils.FIRST_STORE_FILE_NAME;
import static com.triple.util.CommonUtils.PLACE_REGISTRANT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.SECOND_STORE_FILE_NAME;
import static io.restassured.RestAssured.given;

@DisplayName("포인트 관리")
public class PointAcceptanceTest extends AcceptanceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PhotoRepository photoRepository;

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
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);

        ExtractableResponse<Response> response = 포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        포인트_적립됨(response);
    }

    /**
     * Given 리뷰 내용과 사진 첨부 리뷰 작성됨
     * And 포인트 적립됨
     * And 리뷰 수정됨(이미지 삭제)
     * When 포인트 적립 요청
     * Then 포인트 적립됨
     */
    @Test
    void 리뷰_수정_이벤트_포인트_적립() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);
        User userReviewer = 회원_생성됨(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);
        포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());
        List<UUID> deleteAttachedPhotoIds = 리뷰_이미지_삭제됨(review);

        ExtractableResponse<Response> response =
                포인트_적립_요청(given(), ActionType.MOD, deleteAttachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        포인트_적립됨(response);
    }

    private User 회원_생성됨(String accountId) {
        return userRepository.save(new User(accountId));
    }

    private Place 장소_생성(User user) {
        return placeRepository.save(new Place(DEFAULT_ADDRESS, DEFAULT_PLACE_NAME, user));
    }

    private Review 리뷰_생성됨(User user, Place place) {
        return reviewRepository.save(new Review(user, place));
    }

    private List<UUID> 리뷰_이미지_생성됨(Review review) {
        List<Photo> photos = photoRepository.saveAll(Arrays.asList(
                new Photo(DEFAULT_ORIGIN_FILE_NAME, FIRST_STORE_FILE_NAME, review),
                new Photo(DEFAULT_ORIGIN_FILE_NAME, SECOND_STORE_FILE_NAME, review)
                )
        );
        return photos.stream()
                .map(Photo::getId)
                .collect(Collectors.toList());
    }

    private List<UUID> 리뷰_이미지_삭제됨(Review review) {
        photoRepository.deleteByReview(review);
        return photoRepository.findAllByReview(review).stream()
                .map(Photo::getId)
                .collect(Collectors.toList());
    }
}
