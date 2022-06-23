package com.triple.unit;

import com.triple.domain.ActionType;
import com.triple.domain.EventType;
import com.triple.domain.Place;
import com.triple.domain.Point;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PlaceRepository;
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

import static com.triple.util.CommonUtils.DEFAULT_CONTENT;
import static com.triple.util.CommonUtils.FIRST_REVIEWER_ACCOUNT_ID;
import static com.triple.util.CommonUtils.PLACE_REGISTRANT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.SECOND_REVIEWER_ACCOUNT_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class PointServiceTest extends UnitTest {
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
        Place place = createPlace(userPlaceRegistrant);

        User userReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userReviewer, place);

        List<UUID> attachedPhotoIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        PointRequest pointRequest = new PointRequest(EventType.REVIEW, ActionType.ADD, DEFAULT_CONTENT,
                attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        // when
        Point point = pointService.actionPoint(pointRequest);

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    @Test
    void 첫_리뷰_내용_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant);

        User userReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userReviewer, place);

        List<UUID> attachedPhotoIds = null;
        PointRequest pointRequest = new PointRequest(EventType.REVIEW, ActionType.ADD, DEFAULT_CONTENT,
                attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        // when
        Point point = pointService.actionPoint(pointRequest);

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @DisplayName("첫 리뷰가 아니고 리뷰 내용 + 사진 첨부 리뷰 포인트 적립")
    @Test
    void 리뷰_내용과_사진_첨부_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant);
        createFirstReview(place);

        User userSecondReviewer = createUser(SECOND_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userSecondReviewer, place);

        List<UUID> attachedPhotoIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        PointRequest pointRequest = new PointRequest(EventType.REVIEW, ActionType.ADD, DEFAULT_CONTENT,
                attachedPhotoIds, review.getId(), userSecondReviewer.getId(), place.getId());

        // when
        Point point = pointService.actionPoint(pointRequest);

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @DisplayName("첫 리뷰가 아니고 리뷰 내용만으로 포인트 적립")
    @Test
    void 리뷰_내용_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant);
        createFirstReview(place);

        User userSecondReviewer = createUser(SECOND_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userSecondReviewer, place);

        List<UUID> attachedPhotoIds = null;
        PointRequest pointRequest = new PointRequest(EventType.REVIEW, ActionType.ADD, DEFAULT_CONTENT,
                attachedPhotoIds, review.getId(), userSecondReviewer.getId(), place.getId());

        // when
        Point point = pointService.actionPoint(pointRequest);

        // then
        assertThat(point.getScore()).isEqualTo(1);
    }

    private void createFirstReview(Place place) {
        User userFirstReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userFirstReviewer, place);

        List<UUID> attachedPhotoIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        PointRequest pointRequest = new PointRequest(EventType.REVIEW, ActionType.ADD, DEFAULT_CONTENT,
                attachedPhotoIds, review.getId(), userFirstReviewer.getId(), place.getId());

        pointService.actionPoint(pointRequest);
    }

    private User createUser(String accountId) {
        return userRepository.save(new User(accountId));
    }

    private Place createPlace(User user) {
        return placeRepository.save(new Place(user));
    }

    private Review createReview(User user, Place place) {
        return reviewRepository.save(new Review(user, place));
    }
}
