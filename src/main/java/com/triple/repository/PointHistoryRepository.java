package com.triple.repository;

import com.triple.domain.Place;
import com.triple.domain.PointHistory;
import com.triple.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {
    @Query("select h from PointHistory h where h.createDate = (select max(h.createDate) from PointHistory h where h.place = :place)")
    Optional<PointHistory> findByPlaceAndLatest(@Param("place") Place place);

    @Query("select h from PointHistory h where h.createDate = (select max(h.createDate) from PointHistory h where h.review = :review)")
    Optional<PointHistory> findByReviewAndLatest(@Param("review") Review review);
}
