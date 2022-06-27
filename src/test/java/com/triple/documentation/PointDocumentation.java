package com.triple.documentation;

import com.triple.domain.ActionType;
import com.triple.domain.Photo;
import com.triple.domain.Place;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PlaceRepository;
import com.triple.repository.ReviewRepository;
import com.triple.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.payload.FieldDescriptor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.triple.acceptance.PointStepsRequest.포인트_적립_요청;
import static com.triple.acceptance.PointStepsRequest.포인트_조회_요청;
import static com.triple.util.CommonUtils.ADDRESS1;
import static com.triple.util.CommonUtils.ORIGIN_FILE_NAME;
import static com.triple.util.CommonUtils.PLACE_NAME;
import static com.triple.util.CommonUtils.PLACE_REGISTRANT_ACCOUNT_ID;
import static com.triple.util.CommonUtils.REVIEWER_ACCOUNT_ID1;
import static com.triple.util.CommonUtils.STORE_FILE_NAME1;
import static com.triple.util.CommonUtils.STORE_FILE_NAME2;
import static io.restassured.RestAssured.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

public class PointDocumentation extends Documentation {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Test
    void 포인트_적립() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);

        FieldDescriptor[] requestFieldDescriptors = {
                fieldWithPath("type").description("이벤트 타입 (REVIEW)"),
                fieldWithPath("action").description("이벤트 타입의 행위 (ADD, MOD, DELETE)"),
                fieldWithPath("reviewId").description("리뷰 ID (UUID)"),
                fieldWithPath("content").description("리뷰 내용"),
                fieldWithPath("attachedPhotoIds").description("리뷰 사진 첨부 파일 ID 배열 (UUID)"),
                fieldWithPath("userId").description("사용자 ID (UUID)"),
                fieldWithPath("placeId").description("장소 ID (UUID)"),
        };

        포인트_적립_요청(givenRestDocsRequestFields("point-action", requestFieldDescriptors),
                ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId()
        );
    }

    @Test
    void 포인트_조회() {
        User userPlaceRegistrant = 회원_생성됨(PLACE_REGISTRANT_ACCOUNT_ID);
        Place place = 장소_생성(userPlaceRegistrant);
        User userReviewer = 회원_생성됨(REVIEWER_ACCOUNT_ID1);
        Review review = 리뷰_생성됨(userReviewer, place);
        List<UUID> attachedPhotoIds = 리뷰_이미지_생성됨(review);
        포인트_적립_요청(given(), ActionType.ADD, attachedPhotoIds, review.getId(), userReviewer.getId(), place.getId());

        FieldDescriptor[] requestFieldDescriptors = {
                fieldWithPath("userId").description("사용자 ID (UUID)")
        };
        FieldDescriptor[] responseFieldDescriptors = {
                fieldWithPath("id").description("포인트 ID (UUID)"),
                fieldWithPath("score").description("적립 포인트"),
                fieldWithPath("userId").description("사용자 ID (UUID)")
        };

        포인트_조회_요청(givenRestDocsRequestAndResponseFields("point-find", requestFieldDescriptors, responseFieldDescriptors), userReviewer.getId());
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

    private List<UUID> 리뷰_이미지_생성됨(Review review) {
        List<Photo> photos = Arrays.asList(
                new Photo(ORIGIN_FILE_NAME, STORE_FILE_NAME1, review),
                new Photo(ORIGIN_FILE_NAME, STORE_FILE_NAME2, review)
        );
        return photos.stream()
                .map(Photo::getId)
                .collect(Collectors.toList());
    }
}
