package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.store.StoreRatingInfo;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Order.OrderStatus;
import com.sparta.bedelivery.entity.Review;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.OrderRepository;
import com.sparta.bedelivery.repository.ReviewRepository;
import com.sparta.bedelivery.repository.StoreRepository;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.dto.review.ReviewCreateRequest;
import com.sparta.bedelivery.dto.review.ReviewModifyRequest;
import com.sparta.bedelivery.dto.review.ReviewCreateResponse;
import com.sparta.bedelivery.dto.review.ReviewModifyResponse;
import com.sparta.bedelivery.dto.store.StoreReviewResponse;
import com.sparta.bedelivery.dto.user.UserReviewResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final StoreRepository storeRepository;
    private final HashOperations<String, String, String> hashOperations; //redis에 저장시 사용함.

    // 6.1 리뷰 생성
    @Transactional
    public ReviewCreateResponse createReview(String userId, ReviewCreateRequest reviewCreateRequest) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 이미 해당 주문에 대한 리뷰가 작성되었다면 예외 처리한다.
        if(reviewRepository.existsByOrderId(reviewCreateRequest.getOrderId())){
            throw new IllegalArgumentException("리뷰가 이미 작성 되었습니다.");
        }

        Order order = orderRepository.findById(reviewCreateRequest.getOrderId())
                .orElseThrow(()-> new IllegalArgumentException("주문내역을 찾을 수 없습니다."));

        // 주문 정보에 있는 유저와 요청하는 유저가 일치하지 않을 경우 예외 처리한다.
        if(!order.getUserId().equals(user.getUserId())){
            throw new IllegalArgumentException("주문정보의 유자와 일치하지 않습니다.");
        }

        // 해당 주문이 완료된 상태가 아닌 경우 리뷰 작성 못하게 예외 처리한다.
        if(!order.getStatus().equals(OrderStatus.COMPLETED)){
            throw new IllegalArgumentException("주문 완료상태가 아닙니다.");
        }

        // 리뷰 정보를 저장한다.
        Review review = reviewRepository.save(new Review(reviewCreateRequest, user, order));

        // 응답할 리뷰 정보를 생성후 return한다.
        return new ReviewCreateResponse(review);
    }

    // 6.2 매장의 모든 리뷰 조회
    @Transactional(readOnly = true)
    public Page<StoreReviewResponse> getStoreReviews(UUID storeId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Review> storeReviews = reviewRepository.findByStoreIdAndDeleteAtIsNull(storeId, pageable);

        return storeReviews.map(StoreReviewResponse::new);
    }

    // 6.3 사용자 리뷰 조회 - 유저가 작성한 모든 리뷰 조회
    @Transactional(readOnly = true)
    public Page<UserReviewResponse> getUserReviews(String userId, int page, int size) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));

        Page<Review> userReviews = reviewRepository.findByUserIdAndDeleteAtIsNull(user.getId(), pageable);

        return userReviews.map(UserReviewResponse::new);
    }



    //6.4 리뷰 수정
    @Transactional
    public ReviewModifyResponse modifyReview(String userId, UUID reviewId, ReviewModifyRequest reviewModifyRequest) {
        // 리뷰 존재 여부 확인
        Review review = reviewRepository.findByIdAndDeleteAtIsNull(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        // 리뷰 작성자가 맞는지 확인
        if (!review.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 수정할 수 있습니다.");
        }

        // 리뷰 내용 수정
        review.setRating(reviewModifyRequest.getRating());
        review.setComment(reviewModifyRequest.getComment());
        reviewRepository.save(review);

        return new ReviewModifyResponse(review);
    }

    // 6.5 리뷰 삭제(사용자용)
    @Transactional
    public void deleteReview(String userId, UUID reviewId) {
        // 리뷰 존재 여부 확인
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

        // 리뷰 작성자가 맞는지 확인
        if (!review.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
        }

        // 소프트 삭제 처리
        review.delete(userId);
        reviewRepository.save(review);
    }

    @Transactional(readOnly = true)
    public void updateStoreRatingsInRedis() {
        // 삭제되지 않은 매장 목록 조회
        List<Store> stores = storeRepository.findAllByDeleteAtIsNull();

        // Store 리스트에서 UUID 리스트로 변환
        List<UUID> storeIds = stores.stream()
                .map(Store::getId)
                .collect(Collectors.toList());

        // 평균 별점과 리뷰 개수 조회
        List<StoreRatingInfo> results = reviewRepository.findAverageRatingAndReviewCountByStoreIds(storeIds);

        // Redis에 저장
        for (StoreRatingInfo info : results) {
            UUID storeId = info.getStoreId();
            String avgRating = info.getAverageRating().toString();
            Long reviewCount = info.getReviewCount();

            // Redis 해시에 저장할 데이터 맵 생성
            Map<String, String> ratingData = new HashMap<>();
            ratingData.put("rating", avgRating);
            ratingData.put("reviewCount", reviewCount.toString());

            // Redis 해시에 데이터 저장 (키: store:{storeId}:reviewInfo)
            String redisKey = "store:" + storeId + ":reviewInfo";
            hashOperations.putAll(redisKey, ratingData);
        }

    }

    // 매장의 리뷰 개수와 평균 별점을 조회하는 메서드
    public Map<String, String> getStoreReviewInfo(UUID storeId) {
        String redisKey = "store:" + storeId + ":reviewInfo";
        return hashOperations.entries(redisKey);
    }
}
