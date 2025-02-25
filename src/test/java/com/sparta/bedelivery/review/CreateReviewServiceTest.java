package com.sparta.bedelivery.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.sparta.bedelivery.dto.ReviewCreateRequest;
import com.sparta.bedelivery.dto.ReviewCreateResponse;
import com.sparta.bedelivery.entity.Order;
import com.sparta.bedelivery.entity.Review;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.OrderRepository;
import com.sparta.bedelivery.repository.ReviewRepository;
import com.sparta.bedelivery.repository.StoreRepository;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.service.ReviewService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CreateReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StoreRepository storeRepository;

    private String userId;
    private UUID orderId;
    private ReviewCreateRequest request;
    private User mockUser;
    private Order mockOrder;
    private Store mockStore;
    private Review mockReview;

    @BeforeEach
    void setUp() {
        userId = "testUser123";
        orderId = UUID.randomUUID();
        request = new ReviewCreateRequest(orderId, 5, "좋은 서비스였습니다.");

        mockUser = new User();
        mockUser.setUserId(userId);

        mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setUserId(userId);

        mockStore = new Store();
        mockOrder.setStore(mockStore);

        mockReview = new Review(request, mockUser, mockOrder);
    }

    @Test
    @DisplayName("리뷰 생성 - 성공")
    void createReview_Success(){
        /**
         * given
         */
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.existsByOrderId(orderId)).thenReturn(false);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(reviewRepository.save(any(Review.class))).thenReturn(mockReview);

        /**
         * when
         */
        ReviewCreateResponse response= reviewService.createReview(userId, request);

        /**
         * then
         */
        assertNotNull(response, "응답 객체가 null이면 안됨");
        assertEquals(mockReview.getId(), response.getReviewId(),"리뷰 ID가 일치 해야함");
        assertEquals(mockReview.getComment(), response.getComment(),"댓글 내용이 일치 해야함");
        assertEquals(mockReview.getRating(), response.getRating(),"리뷰 평점이 일치해야함");

        // 성공 시
        System.out.println("리뷰 생성 성공 테스트 통과");
        System.out.println("리뷰 ID: " + response.getReviewId());
        System.out.println("리뷰 내용: " + response.getComment());
        System.out.println("별점: " + response.getRating());
    }

    @Test
    @DisplayName("리뷰 생성 - 실패 - 이미 해당 Order에 대한 리뷰가 존재할 경우")
    void createReview_fail_existsByOrderReview(){
        /**
         * given
         */
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(mockUser));
        when(reviewRepository.existsByOrderId(orderId)).thenReturn(true); // 이미 리뷰가 존재

        /**
         * when & then
         */
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> reviewService.createReview(userId, request), "리뷰가 이미 존재할 경우 예외가 발생해야합니다.");

        System.out.println("리뷰 생성 실패 테스트 통과");
        System.out.println("예외 메시지 : "+exception.getMessage());
    }
}
