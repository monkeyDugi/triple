package com.triple.web;

import com.triple.service.PointServiceRouter;
import com.triple.web.dto.ApiResponse;
import com.triple.web.dto.PointFindRequest;
import com.triple.web.dto.PointResponse;
import com.triple.web.dto.PointSaveRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {
    private final PointServiceRouter pointServiceRouter;

    public PointController(PointServiceRouter pointServiceRouter) {
        this.pointServiceRouter = pointServiceRouter;
    }
    @PostMapping(value = "/events", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> actionPoint(@RequestBody PointSaveRequest pointSaveRequest) {
        pointServiceRouter.actionPoint(pointSaveRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/point", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<PointResponse>> findPoint(@RequestBody PointFindRequest pointFindRequest) {
        PointResponse pointResponse = pointServiceRouter.findPoint(pointFindRequest.getUserId());
        return ResponseEntity.ok(ApiResponse.success(pointResponse));
    }
}
