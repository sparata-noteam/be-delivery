package com.sparta.bedelivery.review.service;

import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Review;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.OrderRepository;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.review.dto.ReviewCreateRequest;
import com.sparta.bedelivery.review.dto.ReviewCreateResponse;
import com.sparta.bedelivery.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

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

}
