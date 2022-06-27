package com.triple.service.review;

import com.triple.domain.EventType;
import com.triple.domain.Place;
import com.triple.domain.Point;
import com.triple.domain.PointHistory;
import com.triple.domain.Review;
import com.triple.domain.User;
import com.triple.repository.PointHistoryRepository;
import com.triple.repository.PointRepository;
import com.triple.service.PlaceService;
import com.triple.service.PointService;
import com.triple.service.ReviewService;
import com.triple.service.UserService;
import com.triple.service.review.calculator.CalculatorContext;
import com.triple.web.dto.PointSaveRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class ReviewPointService implements PointService {
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final UserService userService;
    private final ReviewService reviewService;
    private final PlaceService placeService;
    private final CalculatorContext calculatorContext;

    public ReviewPointService(PointRepository pointRepository, PointHistoryRepository pointHistoryRepository, UserService userService, ReviewService reviewService, PlaceService placeService, CalculatorContext calculatorContext) {
        this.pointRepository = pointRepository;
        this.pointHistoryRepository = pointHistoryRepository;
        this.userService = userService;
        this.reviewService = reviewService;
        this.placeService = placeService;
        this.calculatorContext = calculatorContext;
    }

    @Override
    public boolean isSupportedType(EventType eventType) {
        return EventType.REVIEW == eventType;
    }

    @Override
    public void actionPoint(PointSaveRequest pointSaveRequest) {
        User user = userService.findById(pointSaveRequest.getUserId());
        Place place = placeService.findById(pointSaveRequest.getPlaceId(), user);
        Review review = reviewService.findById(pointSaveRequest.getReviewId(), user, place);

        int score = calculateScore(pointSaveRequest, user, review, place);
        Point point = pointRepository.findByUserId(user.getId())
                .orElse(new Point(user));

        point.updateScore(score);
        pointRepository.save(point);

        savePointHistory(pointSaveRequest, user, review, place, score);
    }

    private int calculateScore(PointSaveRequest pointSaveRequest, User user, Review review, Place place) {
        return calculatorContext.calculate(pointSaveRequest, user.getId(), review.getId(), place.getId());
    }

    private void savePointHistory(PointSaveRequest pointSaveRequest, User user, Review review, Place place, int score) {
        PointHistory pointHistory = new PointHistory(score, pointSaveRequest.getAction(), pointSaveRequest.getPhotoCount(),
                review, user, place);
        pointHistoryRepository.save(pointHistory);
    }
}
