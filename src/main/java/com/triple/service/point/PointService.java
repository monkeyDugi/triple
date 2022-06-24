package com.triple.service.point;

import com.triple.domain.Place;
import com.triple.domain.Point;
import com.triple.domain.PointHistory;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PointHistoryRepository;
import com.triple.repository.PointRepository;
import com.triple.service.PlaceService;
import com.triple.service.ReviewService;
import com.triple.service.UserService;
import com.triple.service.point.calculator.CalculatorContext;
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

    public void actionPoint(PointRequest pointRequest) {
        User user = userService.findById(pointRequest.getUserId());
        Place place = placeService.findById(pointRequest.getPlaceId(), user);
        Review review = reviewService.findById(pointRequest.getReviewId());

        Point point = pointRepository.findByUser(user)
                .orElse(new Point(user));
        int score = calculateScore(pointRequest, review, place, point);

        point.updateScore(score);
        pointRepository.save(point);

        savePointHistory(pointRequest, user, review, place, score);
    }

    private int calculateScore(PointRequest pointRequest, Review review, Place place, Point point) {
        return new CalculatorContext(pointHistoryRepository)
                .calculate(pointRequest, review, place, point.getScore());
    }

    private void savePointHistory(PointRequest pointRequest, User user, Review review, Place place, int score) {
        PointHistory pointHistory = new PointHistory(score, pointRequest.getAction(),
                pointRequest.getContent(), pointRequest.getPhotoCount(),
                review, user, place);
        pointHistoryRepository.save(pointHistory);
    }
}
