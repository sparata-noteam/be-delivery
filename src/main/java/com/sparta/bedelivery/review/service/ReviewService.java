package com.sparta.bedelivery.review.service;

import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Review;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.OrderRepository;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.review.dto.ReviewCreateRequest;
import com.sparta.bedelivery.review.dto.ReviewModifyRequest;
import com.sparta.bedelivery.review.dto.ReviewCreateResponse;
import com.sparta.bedelivery.review.dto.ReviewModifyResponse;
import com.sparta.bedelivery.review.dto.StoreReviewResponse;
import com.sparta.bedelivery.review.dto.UserReviewResponse;
import com.sparta.bedelivery.review.repository.ReviewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

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

        Store store = order.getStore();

        // 주문 정보에 있는 유저와 요청하는 유저가 일치하지 않을 경우 예외 처리한다.
        if(!order.getUser().equals(user)){
            throw new IllegalArgumentException("주문정보의 유자와 일치하지 않습니다.");
        }

        // 리뷰 정보를 저장한다.
        Review review = reviewRepository.save(new Review(reviewCreateRequest, user, order, store));

        // 응답할 리뷰 정보를 생성후 return한다.
        return new ReviewCreateResponse(review);
    }

    // 6.2 매장의 모든 리뷰 조회
    @Transactional(readOnly = true)
    public List<StoreReviewResponse> getStoreReivews(UUID storeId) {

        List<Review> storeReviews = reviewRepository.findByStoreIdAndDeleteAtIsNull(storeId);

        List<StoreReviewResponse> reviewResponseList=new ArrayList<>(); //응답할 review들을 담은 리스트

        // Review -> ReviewResponseDTO로 변환 후 리스트에 추가
        for (Review storeReivew : storeReviews) {
            reviewResponseList.add(new StoreReviewResponse(storeReivew));
        }
        return reviewResponseList;
    }

    // 6.3 사용자 리뷰 조회 - 유저가 작성한 모든 리뷰 조회
    public List<UserReviewResponse> getUserReviews(String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Review> userReviews = reviewRepository.findByUserIdAndDeleteAtIsNull(user.getId());
        List<UserReviewResponse> userReviewResponseList = new ArrayList<>();

        //Review-> userReviewResponse로 변환후 리스트에 추가
        for (Review userReview : userReviews) {
            userReviewResponseList.add(new UserReviewResponse (userReview));
        }

        //리스트 반환
        return userReviewResponseList;
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
}
