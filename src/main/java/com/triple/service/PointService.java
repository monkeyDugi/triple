package com.triple.service;

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

    public void actionPoint(PointRequest pointRequest) {
        User user = userService.findById(pointRequest.getUserId());
        Place place = placeService.findById(pointRequest.getPlaceId(), user);
        Review review = reviewService.findById(pointRequest.getReviewId());

        int score = calculateScore(pointRequest, review, place);
        Point point = pointRepository.findByUser(user)
                .orElse(new Point(user));

        point.updateScore(score);
        pointRepository.save(point);

        savePointHistory(pointRequest, user, review, place, score);
    }

    private int calculateScore(PointRequest pointRequest, Review review, Place place) {
        return new CalculatorContext(pointHistoryRepository)
                .calculate(pointRequest, review, place);
    }

    private void savePointHistory(PointRequest pointRequest, User user, Review review, Place place, int score) {
        PointHistory pointHistory = new PointHistory(score, pointRequest.getAction(),
                pointRequest.getContent(), pointRequest.getPhotoCount(),
                review, user, place);
        pointHistoryRepository.save(pointHistory);
    }
}
