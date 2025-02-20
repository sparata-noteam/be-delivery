package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.Review;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


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
}
