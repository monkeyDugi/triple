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

    public Point actionPoint(PointRequest pointRequest) {
        User user = userService.findById(pointRequest.getUserId());
        Point point = new Point(3, user);
        Review review = reviewService.findById(pointRequest.getReviewId());
        Place place = placeService.findById(pointRequest.getPlaceId());

        PointHistory pointHistory = new PointHistory(3, pointRequest.getAction(), pointRequest.getContent(),
                pointRequest.getPhotoCount(), review, user, place);
        pointHistoryRepository.save(pointHistory);

        return pointRepository.save(point);
    }
}
