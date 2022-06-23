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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.triple.util.CommonUtils.DEFAULT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.DEFAULT_CONTENT;
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
        User user = createUser();
        Place place = createPlace(user);
        Review review = createReview(user, place);

        List<UUID> attachedPhotoIds = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        PointRequest pointRequest = new PointRequest(EventType.REVIEW, ActionType.ADD, DEFAULT_CONTENT,
                attachedPhotoIds, review.getId(), user.getId(), place.getId());

        // when
        Point point = pointService.actionPoint(pointRequest);

        // then
        assertThat(point.getScore()).isEqualTo(3);
    }

    private User createUser() {
        return userRepository.save(new User(DEFAULT_ACCOUNT_ID));
    }

    private Place createPlace(User user) {
        return placeRepository.save(new Place(user));
    }

    private Review createReview(User user, Place place) {
        return reviewRepository.save(new Review(user, place));
    }
}
