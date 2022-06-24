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
import com.triple.web.dto.PointRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.triple.util.CommonUtils.DEFAULT_ADDRESS;
import static com.triple.util.CommonUtils.DEFAULT_CONTENT;
import static com.triple.util.CommonUtils.DEFAULT_ORIGIN_FILE_NAME;
import static com.triple.util.CommonUtils.DEFAULT_PLACE_NAME;
import static com.triple.util.CommonUtils.FIRST_REVIEWER_ACCOUNT_ID;
import static com.triple.util.CommonUtils.FIRST_STORE_FILE_NAME;
import static com.triple.util.CommonUtils.PLACE_REGISTRANT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.SECOND_ADDRESS;
import static com.triple.util.CommonUtils.SECOND_REVIEWER_ACCOUNT_ID;
import static com.triple.util.CommonUtils.SECOND_STORE_FILE_NAME;
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
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);

        User userReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userReviewer, place);

        PointRequest pointRequest = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(review), review.getId(), userReviewer.getId(), place.getId()
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
                ActionType.ADD, createAttachedPhotoIds(review), review.getId(), userSecondReviewer.getId(), place.getId()
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

    @DisplayName("다른 장소에 같은 사용자가 리뷰 생성 시 포인트 누적")
    @Test
    void 누적_포인트_적립_다른_장소에_추가_첫_리뷰() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place1 = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        Place place2 = createPlace(userPlaceRegistrant, SECOND_ADDRESS, DEFAULT_PLACE_NAME);

        User userReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);

        Review reviewPlace1 = createReview(userReviewer, place1);
        PointRequest pointRequestPlace1 = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(reviewPlace1), reviewPlace1.getId(), userReviewer.getId(), place1.getId()
        );

        pointService.actionPoint(pointRequestPlace1);

        Review reviewPlace2 = createReview(userReviewer, place2);
        PointRequest pointRequestPlace2 = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(reviewPlace2), reviewPlace2.getId(), userReviewer.getId(), place2.getId()
        );

        // when
        pointService.actionPoint(pointRequestPlace2);
        Point point = pointRepository.findByUser(userReviewer).get();

        // then
        assertThat(point.getScore()).isEqualTo(6);
    }

    @Test
    void 누적_포인트_차감_첫_리뷰_수정_이미지_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        User user = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(user, place);

        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review),
                        review.getId(), user.getId(), place.getId())
        );

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.MOD, deleteAttachedPhotoIds(review),
                        review.getId(), user.getId(), place.getId())
        );
        Point point = pointRepository.findByUser(user).get();

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @Test
    void 누적_포인트_차감_리뷰_수정_이미지_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        createFirstReview(place);

        User user = createUser(SECOND_REVIEWER_ACCOUNT_ID);
        Review review = createReview(user, place);

        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review),
                        review.getId(), user.getId(), place.getId())
        );

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.MOD, deleteAttachedPhotoIds(review),
                        review.getId(), user.getId(), place.getId())
        );
        Point point = pointRepository.findByUser(user).get();

        // then
        assertThat(point.getScore()).isEqualTo(1);
    }

    @Test
    void 누적_포인트_차감_첫_리뷰_수정_이미지_추가() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        User user = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(user, place);

        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, deleteAttachedPhotoIds(review),
                        review.getId(), user.getId(), place.getId())
        );

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.MOD, createAttachedPhotoIds(review),
                        review.getId(), user.getId(), place.getId())
        );
        Point point = pointRepository.findByUser(user).get();

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    @Test
    void 누적_포인트_차감_리뷰_수정_이미지_추가() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        createFirstReview(place);

        User user = createUser(SECOND_REVIEWER_ACCOUNT_ID);
        Review review = createReview(user, place);

        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, deleteAttachedPhotoIds(review),
                        review.getId(), user.getId(), place.getId())
        );

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.MOD, createAttachedPhotoIds(review),
                        review.getId(), user.getId(), place.getId())
        );
        Point point = pointRepository.findByUser(user).get();

        // then
        assertThat(point.getScore()).isEqualTo(2);
    }

    @DisplayName("첫 리뷰로 두 장소에 등록 후 한 장소 리뷰 삭제")
    @Test
    void 누적_포인트_차감_두_장소_첫_리뷰_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place1 = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        Place place2 = createPlace(userPlaceRegistrant, SECOND_ADDRESS, DEFAULT_PLACE_NAME);

        User user = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review1 = createReview(user, place1);
        Review review2 = createReview(user, place2);

        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review1);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review1.getId(), user.getId(), place1.getId())
        );
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review2),
                        review2.getId(), user.getId(), place2.getId())
        );
        deleteReview(review1);

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review1.getId(), user.getId(), place1.getId())
        );
        Point point = pointRepository.findByUser(user).get();

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    @Test
    void 누적_포인트_차감_첫_리뷰_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        User user = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(user, place);

        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review.getId(), user.getId(), place.getId())
        );
        deleteReview(review);

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review.getId(), user.getId(), place.getId())
        );
        Point point = pointRepository.findByUser(user).get();

        // then
        assertThat(point.getScore()).isEqualTo(0);
    }

    @Test
    void 누적_포인트_차감_리뷰_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        createFirstReview(place);

        User user = createUser(SECOND_REVIEWER_ACCOUNT_ID);
        Review review = createReview(user, place);

        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review.getId(), user.getId(), place.getId())
        );
        deleteReview(review);

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review.getId(), user.getId(), place.getId())
        );
        Point point = pointRepository.findByUser(user).get();

        // then
        assertThat(point.getScore()).isEqualTo(0);
    }

    @DisplayName("첫 리뷰가 아닌 장소와 첫 리뷰인 장소 중 첫 리뷰가 아닌 리뷰 삭제")
    @Test
    void 누적_포인트_차감_두_장소_리뷰_삭제() {
        // given
        User userPlaceRegistrant = createUser(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place1 = createPlace(userPlaceRegistrant, DEFAULT_ADDRESS, DEFAULT_PLACE_NAME);
        Place place2 = createPlace(userPlaceRegistrant, SECOND_ADDRESS, DEFAULT_PLACE_NAME);
        createFirstReview(place1);

        User user = createUser(SECOND_REVIEWER_ACCOUNT_ID);
        Review review1 = createReview(user, place1);
        Review review2 = createReview(user, place2);

        List<UUID> review1AttachedPhotoIds = createAttachedPhotoIds(review1);
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, review1AttachedPhotoIds,
                        review1.getId(), user.getId(), place1.getId())
        );
        pointService.actionPoint(
                createPointRequest(
                        ActionType.ADD, createAttachedPhotoIds(review2),
                        review2.getId(), user.getId(), place2.getId())
        );
        deleteReview(review1);

        // when
        pointService.actionPoint(
                createPointRequest(
                        ActionType.DELETE, review1AttachedPhotoIds,
                        review1.getId(), user.getId(), place1.getId())
        );
        Point point = pointRepository.findByUser(user).get();

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    private void createFirstReview(Place place) {
        User userFirstReviewer = createUser(FIRST_REVIEWER_ACCOUNT_ID);
        Review review = createReview(userFirstReviewer, place);

        PointRequest pointRequest = createPointRequest(
                ActionType.ADD, createAttachedPhotoIds(review), review.getId(), userFirstReviewer.getId(), place.getId()
        );

        pointService.actionPoint(pointRequest);
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

    private PointRequest createPointRequest(ActionType actionType, List<UUID> attachedPhotoIds, UUID reviewId, UUID userId, UUID placeId) {
        return new PointRequest(EventType.REVIEW, actionType, DEFAULT_CONTENT,
                attachedPhotoIds, reviewId, userId, placeId);
    }

    private List<UUID> createAttachedPhotoIds(Review review) {
        List<Photo> photos = photoRepository.saveAll(Arrays.asList(
                new Photo(DEFAULT_ORIGIN_FILE_NAME, FIRST_STORE_FILE_NAME, review),
                new Photo(DEFAULT_ORIGIN_FILE_NAME, SECOND_STORE_FILE_NAME, review)
                )
        );
        return photos.stream()
                .map(Photo::getId)
                .collect(Collectors.toList());
    }

    private List<UUID> deleteAttachedPhotoIds(Review review) {
        photoRepository.deleteByReview(review);
        return photoRepository.findAllByReview(review).stream()
                .map(Photo::getId)
                .collect(Collectors.toList());
    }
}
