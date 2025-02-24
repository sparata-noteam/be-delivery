package com.sparta.bedelivery.service;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
@Slf4j
public class StoreRatingScheduler {
    private final ReviewService reviewService;

    // todo - 1시간(3600000ms)/60 = 1분마다 실행 - 테스트 목적
    @Scheduled(fixedRate = 3600000)
    public void updateStoreRatingsJob() {
        log.info("[스케줄러 실행] 매장 평균 별점 및 리뷰 개수를 업데이트합니다.");
        reviewService.updateStoreRatingsInRedis();

        log.info("[완료] Redis에 평균 별점과 리뷰 개수 업데이트 완료!");
        log.debug("{}", reviewService.getStoreReviewInfo(UUID.fromString("422bbca4-b4ef-4350-acfe-bf2789dd95e2")));
    }
}
