package com.triple.unit;

import com.triple.domain.ActionType;
import com.triple.domain.EventType;
import com.triple.domain.Place;
import com.triple.domain.Point;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PlaceRepository;
import com.triple.repository.PointRepository;
import com.triple.repository.ReviewRepository;
import com.triple.repository.UserRepository;
import com.triple.service.PointService;
import com.triple.util.UnitTest;
import com.triple.web.dto.PointRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.triple.util.CommonUtils.DEFAULT_ADDRESS;
import static com.triple.util.CommonUtils.DEFAULT_CONTENT;
import static com.triple.util.CommonUtils.DEFAULT_PLACE_NAME;
import static com.triple.util.CommonUtils.FIRST_REVIEWER_ACCOUNT_ID;
import static com.triple.util.CommonUtils.PLACE_REGISTRANT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.SECOND_ADDRESS;
import static com.triple.util.CommonUtils.SECOND_REVIEWER_ACCOUNT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class PointServiceTest extends UnitTest {
    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private PointService pointService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    void 첫_리뷰_내용과_사진_첨부_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);

        User userReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userReviewer, place);

        PointRequest pointRequest = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(), review.getId(), userReviewer.getId(), place.getId()
        );

        // when
        pointService.actionPoint(pointRequest);
        Point point = pointRepository.findByUser(userReviewer).get();

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    @Test
    void 첫_리뷰_내용_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);

        User userReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userReviewer, place);

        PointRequest pointRequest = createPointRequest(
                ActionType.ADD, null, review.getId(), userReviewer.getId(), place.getId()
        );

        // when
        pointService.actionPoint(pointRequest);
        Point point = pointRepository.findByUser(userReviewer).get();

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @DisplayName("첫 리뷰가 아니고 리뷰 내용 + 사진 첨부 리뷰 포인트 적립")
    @Test
    void 리뷰_내용과_사진_첨부_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        createFirstReview(place);

        User userSecondReviewer = createUser(SECOND_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userSecondReviewer, place);

        PointRequest pointRequest = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(), review.getId(), userSecondReviewer.getId(), place.getId()
        );

        // when
        pointService.actionPoint(pointRequest);
        Point point = pointRepository.findByUser(userSecondReviewer).get();

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @DisplayName("첫 리뷰가 아니고 리뷰 내용만으로 포인트 적립")
    @Test
    void 리뷰_내용_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        createFirstReview(place);

        User userSecondReviewer = createUser(SECOND_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userSecondReviewer, place);

        PointRequest pointRequest = createPointRequest(
                ActionType.ADD, null, review.getId(), userSecondReviewer.getId(), place.getId()
        );

        // when
        pointService.actionPoint(pointRequest);
        Point point = pointRepository.findByUser(userSecondReviewer).get();

        // then
        assertThat(point.getScore()).isEqualTo(1);
    }

    @Test
    void 누적_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place1 = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        Place place2 = createPlace(userPlaceRegistrant, SECOND_ADDRESS, DEFAULT_PLACE_NAME);

        User userReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);

        Review reviewPlace1 = createReview(userReviewer, place1);
        PointRequest pointRequestPlace1 = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(), reviewPlace1.getId(), userReviewer.getId(), place1.getId()
        );

        pointService.actionPoint(pointRequestPlace1);

        Review reviewPlace2 = createReview(userReviewer, place2);
        PointRequest pointRequestPlace2 = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(), reviewPlace2.getId(), userReviewer.getId(), place2.getId()
        );

        // when
        pointService.actionPoint(pointRequestPlace2);
        Point point = pointRepository.findByUser(userReviewer).get();

        // then
        assertThat(point.getScore()).isEqualTo(6);
    }

    private void createFirstReview(Place place) {
        User userFirstReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userFirstReviewer, place);

        PointRequest pointRequest = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(), review.getId(), userFirstReviewer.getId(), place.getId()
        );

        pointService.actionPoint(pointRequest);
    }

    private User createUser(String accountId) {
        return userRepository.save(new User(accountId));
    }

    private Place createPlace(User user, String address, String name) {
        return placeRepository.save(new Place(user, address, name));
    }

    private Review createReview(User user, Place place) {
        return reviewRepository.save(new Review(user, place));
    }

    private List<UUID> createAttachedPhotoIds() {
        return Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
    }

    private PointRequest createPointRequest(ActionType actionType, List<UUID> attachedPhotoIds, UUID reviewId, UUID userId, UUID placeId) {
        return new PointRequest(EventType.REVIEW, actionType, DEFAULT_CONTENT,
                attachedPhotoIds, reviewId, userId, placeId);
    }
}
