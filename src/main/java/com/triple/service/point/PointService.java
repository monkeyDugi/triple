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
import com.triple.web.dto.PointResponse;
import com.triple.web.dto.PointSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class PointService {
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final PlaceService placeService;
    private final CalculatorContext calculatorContext;

    public PointService(PointRepository pointRepository, PointHistoryRepository pointHistoryRepository, UserService userService, ReviewService reviewService, PlaceService placeService, CalculatorContext calculatorContext) {
        this.pointRepository = pointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
        this.userService = userService;
        this.reviewService = reviewService;
        this.placeService = placeService;
        this.calculatorContext = calculatorContext;
    }

    public void actionPoint(PointSaveRequest pointSaveRequest) {
        User user = userService.findById(pointSaveRequest.getUserId());
        Place place = placeService.findById(pointSaveRequest.getPlaceId(), user);
        Review review = reviewService.findById(pointSaveRequest.getReviewId(), user, place);

        int score = calculateScore(pointSaveRequest, review, place);
        Point point = pointRepository.findByUserId(user.getId())
                .orElse(new Point(user));

        point.updateScore(score);
        pointRepository.save(point);

        savePointHistory(pointSaveRequest, user, review, place, score);
    }

    private int calculateScore(PointSaveRequest pointSaveRequest, Review review, Place place) {
        return calculatorContext.calculate(pointSaveRequest, review.getId(), place.getId());
    }

    private void savePointHistory(PointSaveRequest pointSaveRequest, User user, Review review, Place place, int score) {
        PointHistory pointHistory = new PointHistory(score, pointSaveRequest.getAction(),
                pointSaveRequest.getContent(), pointSaveRequest.getPhotoCount(),
                review, user, place);
        pointHistoryRepository.save(pointHistory);
    }

    public PointResponse findPoint(UUID userId) {
        Point point = pointRepository.findByUserId(userId)
                .orElse(Point.emptyPoint(userId));
        return new PointResponse(point.getId(), point.getScore(), point.getUser().getId());
    }
}
