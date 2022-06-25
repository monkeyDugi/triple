package com.triple.repository;

import com.triple.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {
    @Query("select h from PointHistory h where h.createDate = (select max(h.createDate) from PointHistory h where h.place.id = :placeId)")
    Optional<PointHistory> findByPlaceIdAndLatest(@Param("placeId") UUID placeId);

    @Query("select h from PointHistory h where h.createDate = (select max(h.createDate) from PointHistory h where h.review.id = :reviewId)")
    Optional<PointHistory> findByReviewIdAndLatest(@Param("reviewId") UUID reviewId);
}
