package com.triple.unit;

import com.triple.domain.ActionType;
import com.triple.domain.EventType;
import com.triple.domain.Photo;
import com.triple.domain.Place;
import com.triple.domain.Point;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PhotoRepository;
import com.triple.repository.PlaceRepository;
import com.triple.repository.PointRepository;
import com.triple.repository.ReviewRepository;
import com.triple.repository.UserRepository;
import com.triple.service.point.PointService;
import com.triple.util.UnitTest;
import com.triple.web.dto.PointSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.triple.util.CommonUtils.ADDRESS1;
import static com.triple.util.CommonUtils.ADDRESS2;
import static com.triple.util.CommonUtils.CONTENT;
import static com.triple.util.CommonUtils.ORIGIN_FILE_NAME;
import static com.triple.util.CommonUtils.PLACE_NAME;
import static com.triple.util.CommonUtils.PLACE_REGISTRANT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.REVIEWER_ACCOUNT_ID1;
import static com.triple.util.CommonUtils.REVIEWER_ACCOUNT_ID2;
import static com.triple.util.CommonUtils.STORE_FILE_NAME1;
import static com.triple.util.CommonUtils.STORE_FILE_NAME2;
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

    @Autowired
    private PhotoRepository photoRepository;

    @Test
    void 첫_리뷰_내용과_사진_첨부_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);

        User userReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        Review review = createReview(userReviewer, place);

        PointSaveRequest pointSaveRequest = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(review), review.getId(), userReviewer.getId(), place.getId()
        );

        // when
        pointService.actionPoint(pointSaveRequest);
        Point point = pointRepository.findByUserId(userReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    @Test
    void 첫_리뷰_내용_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);

        User userReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        Review review = createReview(userReviewer, place);

        PointSaveRequest pointSaveRequest = createPointRequest(
                ActionType.ADD, null, review.getId(), userReviewer.getId(), place.getId()
        );

        // when
        pointService.actionPoint(pointSaveRequest);
        Point point = pointRepository.findByUserId(userReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @DisplayName("첫 리뷰가 아니고 리뷰 내용 + 사진 첨부 리뷰 포인트 적립")
    @Test
    void 리뷰_내용과_사진_첨부_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        createFirstReview(place);

        User userSecondReviewer = createUser(REVIEWER_ACCOUNT_ID2);
        Review review = createReview(userSecondReviewer, place);

        PointSaveRequest pointSaveRequest = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(review), review.getId(), userSecondReviewer.getId(), place.getId()
        );

        // when
        pointService.actionPoint(pointSaveRequest);
        Point point = pointRepository.findByUserId(userSecondReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @DisplayName("첫 리뷰가 아니고 리뷰 내용만으로 포인트 적립")
    @Test
    void 리뷰_내용_리뷰_생성_이벤트_포인트_적립() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        createFirstReview(place);

        User userSecondReviewer = createUser(REVIEWER_ACCOUNT_ID2);
        Review review = createReview(userSecondReviewer, place);

        PointSaveRequest pointSaveRequest = createPointRequest(
                ActionType.ADD, null, review.getId(), userSecondReviewer.getId(), place.getId()
        );

        // when
        pointService.actionPoint(pointSaveRequest);
        Point point = pointRepository.findByUserId(userSecondReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(1);
    }

    @DisplayName("다른 장소에 같은 사용자가 리뷰 생성 시 포인트 누적")
    @Test
    void 누적_포인트_적립_다른_장소에_추가_첫_리뷰() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place1 = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        Place place2 = createPlace(userPlaceRegistrant, ADDRESS2, PLACE_NAME);

        User userReviewer = createUser(REVIEWER_ACCOUNT_ID1);

        Review reviewPlace1 = createReview(userReviewer, place1);
        PointSaveRequest pointSaveRequestPlace1 = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(reviewPlace1), reviewPlace1.getId(), userReviewer.getId(), place1.getId()
        );

        pointService.actionPoint(pointSaveRequestPlace1);

        Review reviewPlace2 = createReview(userReviewer, place2);
        PointSaveRequest pointSaveRequestPlace2 = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(reviewPlace2), reviewPlace2.getId(), userReviewer.getId(), place2.getId()
        );

        // when
        pointService.actionPoint(pointSaveRequestPlace2);
        Point point = pointRepository.findByUserId(userReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(6);
    }

    @Test
    void 누적_포인트_차감_첫_리뷰_수정_이미지_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        User userReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        Review review = createReview(userReviewer, place);

        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review),
                        review.getId(), userReviewer.getId(), place.getId())
        );

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.MOD, deleteAttachedPhotoIds(review.getId()),
                        review.getId(), userReviewer.getId(), place.getId())
        );
        Point point = pointRepository.findByUserId(userReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @Test
    void 누적_포인트_차감_리뷰_수정_이미지_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        createFirstReview(place);

        User userSecondReviewer = createUser(REVIEWER_ACCOUNT_ID2);
        Review review = createReview(userSecondReviewer, place);

        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review),
                        review.getId(), userSecondReviewer.getId(), place.getId())
        );

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.MOD, deleteAttachedPhotoIds(review.getId()),
                        review.getId(), userSecondReviewer.getId(), place.getId())
        );
        Point point = pointRepository.findByUserId(userSecondReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(1);
    }

    @Test
    void 누적_포인트_차감_첫_리뷰_수정_이미지_추가() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        User userReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        Review review = createReview(userReviewer, place);

        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, deleteAttachedPhotoIds(review.getId()),
                        review.getId(), userReviewer.getId(), place.getId())
        );

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.MOD, createAttachedPhotoIds(review),
                        review.getId(), userReviewer.getId(), place.getId())
        );
        Point point = pointRepository.findByUserId(userReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    @Test
    void 누적_포인트_차감_리뷰_수정_이미지_추가() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        createFirstReview(place);

        User userSecondReviewer = createUser(REVIEWER_ACCOUNT_ID2);
        Review review = createReview(userSecondReviewer, place);

        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, deleteAttachedPhotoIds(review.getId()),
                        review.getId(), userSecondReviewer.getId(), place.getId())
        );

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.MOD, createAttachedPhotoIds(review),
                        review.getId(), userSecondReviewer.getId(), place.getId())
        );
        Point point = pointRepository.findByUserId(userSecondReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @DisplayName("첫 리뷰로 두 장소에 등록 후 한 장소 리뷰 삭제")
    @Test
    void 누적_포인트_차감_두_장소_첫_리뷰_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place1 = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        Place place2 = createPlace(userPlaceRegistrant, ADDRESS2, PLACE_NAME);

        User userReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        Review review1 = createReview(userReviewer, place1);
        Review review2 = createReview(userReviewer, place2);

        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review1);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review1.getId(), userReviewer.getId(), place1.getId())
        );
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review2),
                        review2.getId(), userReviewer.getId(), place2.getId())
        );
        deleteReview(review1);

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review1.getId(), userReviewer.getId(), place1.getId())
        );
        Point point = pointRepository.findByUserId(userReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    @Test
    void 누적_포인트_차감_첫_리뷰_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        User userReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        Review review = createReview(userReviewer, place);

        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review.getId(), userReviewer.getId(), place.getId())
        );
        deleteReview(review);

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review.getId(), userReviewer.getId(), place.getId())
        );
        Point point = pointRepository.findByUserId(userReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(0);
    }

    @Test
    void 누적_포인트_차감_리뷰_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        createFirstReview(place);

        User userSecondReviewer = createUser(REVIEWER_ACCOUNT_ID2);
        Review review = createReview(userSecondReviewer, place);

        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review.getId(), userSecondReviewer.getId(), place.getId())
        );
        deleteReview(review);

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review.getId(), userSecondReviewer.getId(), place.getId())
        );
        Point point = pointRepository.findByUserId(userSecondReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(0);
    }

    @DisplayName("첫 리뷰가 아닌 장소와 첫 리뷰인 장소 중 첫 리뷰가 아닌 리뷰 삭제")
    @Test
    void 누적_포인트_차감_두_장소_리뷰_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place1 = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        Place place2 = createPlace(userPlaceRegistrant, ADDRESS2, PLACE_NAME);
        createFirstReview(place1);

        User userSecondReviewer = createUser(REVIEWER_ACCOUNT_ID2);
        Review review1 = createReview(userSecondReviewer, place1);
        Review review2 = createReview(userSecondReviewer, place2);

        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review1);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review1.getId(), userSecondReviewer.getId(), place1.getId())
        );
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review2),
                        review2.getId(), userSecondReviewer.getId(), place2.getId())
        );
        deleteReview(review1);

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review1.getId(), userSecondReviewer.getId(), place1.getId())
        );
        Point point = pointRepository.findByUserId(userSecondReviewer.getId()).get();

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    @DisplayName("사용자A가 첫 리뷰를 남겼다가 삭제하고, 사용자B가 리뷰 생성 시 사용자B는 첫 리뷰이다.")
    @Test
    void 두_사용자_간_첫_리뷰_포인트_적립1() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        User userFirstReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        User userSecondReviewer = createUser(REVIEWER_ACCOUNT_ID2);

        Review review1 = createReview(userFirstReviewer, place);
        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review1);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review1.getId(), userFirstReviewer.getId(), place.getId())
        );
        deleteReview(review1);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review1.getId(), userFirstReviewer.getId(), place.getId())
        );

        // when
        Review review2 = createReview(userSecondReviewer, place);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review2),
                        review2.getId(), userSecondReviewer.getId(), place.getId())
        );

        Point firstReviewPoint = pointRepository.findByUserId(userFirstReviewer.getId()).get();
        Point secondReviewPoint = pointRepository.findByUserId(userSecondReviewer.getId()).get();

        // then
        assertThat(firstReviewPoint.getScore()).isEqualTo(0);
        assertThat(secondReviewPoint.getScore()).isEqualTo(3);
    }

    @DisplayName("사용자A가 첫 리뷰를 남기고, 사용자B가 리뷰를 생성하고, 사용자A가 리뷰 삭제 시 사용자B는 첫 리뷰가 아니다.")
    @Test
    void 두_사용자_간_첫_리뷰_포인트_적립2() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, ADDRESS1, PLACE_NAME);
        User userFirstReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        User userSecondReviewer = createUser(REVIEWER_ACCOUNT_ID2);

        Review review1 = createReview(userFirstReviewer, place);
        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review1);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review1.getId(), userFirstReviewer.getId(), place.getId())
        );

        // when
        Review review2 = createReview(userSecondReviewer, place);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review2),
                        review2.getId(), userSecondReviewer.getId(), place.getId())
        );

        deleteReview(review1);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review1.getId(), userFirstReviewer.getId(), place.getId())
        );

        Point firstReviewPoint = pointRepository.findByUserId(userFirstReviewer.getId()).get();
        Point secondReviewPoint = pointRepository.findByUserId(userSecondReviewer.getId()).get();

        // then
        assertThat(firstReviewPoint.getScore()).isEqualTo(0);
        assertThat(secondReviewPoint.getScore()).isEqualTo(2);
    }

    private void createFirstReview(Place place) {
        User userFirstReviewer = createUser(REVIEWER_ACCOUNT_ID1);
        Review review = createReview(userFirstReviewer, place);

        PointSaveRequest pointSaveRequest = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(review), review.getId(), userFirstReviewer.getId(), place.getId()
        );

        pointService.actionPoint(pointSaveRequest);
    }

    private User createUser(String accountId) {
        return userRepository.save(new User(accountId));
    }

    private Place createPlace(User user, String address, String name) {
        return placeRepository.save(new Place(address, name, user));
    }

    private Review createReview(User user, Place place) {
        return reviewRepository.save(new Review(user, place));
    }

    private void deleteReview(Review review) {
        ReflectionTestUtils.setField(review, "deleted", true);
        reviewRepository.save(review);
    }

    private PointSaveRequest createPointRequest(ActionType actionType, List<UUID> attachedPhotoIds, UUID reviewId, UUID userId, UUID placeId) {
        return new PointSaveRequest(EventType.REVIEW, actionType, CONTENT,
                attachedPhotoIds, reviewId, userId, placeId);
    }

    private List<UUID> createAttachedPhotoIds(Review review) {
        List<Photo> photos = photoRepository.saveAll(Arrays.asList(
                new Photo(ORIGIN_FILE_NAME, STORE_FILE_NAME1, review),
                new Photo(ORIGIN_FILE_NAME, STORE_FILE_NAME2, review)
                )
        );
        return photos.stream()
                .map(Photo::getId)
                .collect(Collectors.toList());
    }

    private List<UUID> deleteAttachedPhotoIds(UUID reviewId) {
        photoRepository.deleteByReviewId(reviewId);
        return new ArrayList<>();
    }
}
