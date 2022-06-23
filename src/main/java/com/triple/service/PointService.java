package com.triple.service;

import com.triple.domain.ActionType;
import com.triple.domain.Place;
import com.triple.domain.Point;
import com.triple.domain.PointHistory;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PointHistoryRepository;
import com.triple.repository.PointRepository;
import com.triple.web.dto.PointRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class PointService {
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final PlaceService placeService;

    public PointService(PointRepository pointRepository, PointHistoryRepository pointHistoryRepository, UserService userService, ReviewService reviewService, PlaceService placeService) {
        this.pointRepository = pointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
        this.userService = userService;
        this.reviewService = reviewService;
        this.placeService = placeService;
    }

    public Point actionPoint(PointRequest pointRequest) {
        User user = userService.findById(pointRequest.getUserId());
        Review review = reviewService.findById(pointRequest.getReviewId());
        Place place = placeService.findById(pointRequest.getPlaceId());

        int score = calculateScore(place, pointRequest);
        Point point = new Point(score, user);
        PointHistory pointHistory = new PointHistory(score, pointRequest.getAction(), pointRequest.getContent(),
                pointRequest.getPhotoCount(), review, user, place);
        pointHistoryRepository.save(pointHistory);

        return pointRepository.save(point);
    }

    private int calculateScore(Place place, PointRequest pointRequest) {
        return getContentScore(pointRequest) + getFirstReviewBonusScore(place);
    }

    private int getContentScore(PointRequest pointRequest) {
        if (pointRequest.getAction() == ActionType.ADD) {
            if (pointRequest.getPhotoCount() > 0) {
                return 2;
            }
            return 1;
        } else if (pointRequest.getAction() == ActionType.MOD) {

        } else if (pointRequest.getAction() == ActionType.DELETE) {

        }
        return 0;
    }

    private int getFirstReviewBonusScore(Place place) {
        boolean isFirstReview = pointHistoryRepository.findByPlaceAndLatest(place)
                .isPresent();
        if (!isFirstReview) {
            return 1;
        }
        return 0;
    }
}
