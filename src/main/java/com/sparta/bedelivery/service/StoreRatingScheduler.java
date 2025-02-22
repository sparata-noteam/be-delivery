package com.sparta.bedelivery.service;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StoreRatingScheduler {
    private final ReviewService reviewService;

    // todo - 1시간(3600000ms)/60 = 1분마다 실행 - 테스트 목적
    @Scheduled(fixedRate = 3600000/60)
    public void updateStoreRatingsJob() {
        System.out.println("[스케줄러 실행] 매장 평균 별점 및 리뷰 개수를 업데이트합니다.");
        reviewService.updateStoreRatingsInRedis();
        System.out.println("[완료] Redis에 평균 별점과 리뷰 개수 업데이트 완료!");
        System.out.println(reviewService.getStoreReviewInfo(UUID.fromString("422bbca4-b4ef-4350-acfe-bf2789dd95e2")));
    }
}
