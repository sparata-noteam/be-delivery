package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.dto.StoreRatingInfo;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Review;
import com.sparta.bedelivery.entity.Store;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ReviewRepository extends JpaRepository<Review, UUID> {
    boolean existsByOrderId(UUID orderID);

    // 해당 매장의 모든 리뷰 조회
    Page<Review> findByStoreIdAndDeleteAtIsNull(UUID storeId, Pageable pageable);

    // 유저가 작성한 모든 리뷰 조회
    Page<Review> findByUserIdAndDeleteAtIsNull(Long userId, Pageable pageable);

    // 특정 리뷰 조회
    Optional<Review> findByIdAndDeleteAtIsNull(UUID id);

    // 관리자가 전체 리뷰 조회
    Page<Review> findAllByDeleteAtIsNull(Pageable pageable);

    UUID order(Order order);

    // 매장들의 평균 별점과 리뷰 개수를 조회
    @Query("SELECT new com.sparta.bedelivery.dto.StoreRatingInfo(r.store.id, COALESCE(AVG(r.rating), 0.0), COUNT(r)) " +
            "FROM Review r " +
            "WHERE r.store.id IN :storeIds " +
            "GROUP BY r.store.id")
    List<StoreRatingInfo> findAverageRatingAndReviewCountByStoreIds(@Param("storeIds") List<UUID> storeIds);
}
