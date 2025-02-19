package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    // 3.1 매장 등록 요청
    @PostMapping("/stores/{userId}")
    public ResponseEntity<ApiResponseData<StoreResponseDto>> createStoreRequest(@RequestBody StoreRequestDto requestDto
    , @PathVariable String userId) {  // UserDetails 대신 User 엔티티를 직접 받음
        StoreResponseDto newStore = storeService.createStoreRequest(requestDto, userId);

        return ResponseEntity.ok().body(ApiResponseData.success(newStore, "매장 등록 요청이 접수되었습니다."));
    }

    // 3.2 전체 매장 목록 조회 OPEN 상태의 매장만 조회 가능.
    @GetMapping("/stores")
    public ResponseEntity<ApiResponseData<List<StoreResponseDto>>> findOpenStores() {

        List<StoreResponseDto> openStores = storeService.findOpenStores();

        return ResponseEntity.ok().body(ApiResponseData.success(openStores));
    }

    // 3.3 매장 상세 조회 => 메뉴 추가 기능과 리뷰 추가 기능이 있어야 구현 가능할듯.
    @GetMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponseData<List<StoreDetailsResponseDto>>> getAllStores(@PathVariable UUID storeId){
        List<StoreDetailsResponseDto> findAll = storeService.getAllStores(storeId);

        return ResponseEntity.ok().body(ApiResponseData.success(findAll));
    }

    // 3.4 매장 삭제 요청
    @DeleteMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponseData<StoreStatusResponseDto>> deleteStoreRequest(@PathVariable UUID storeId) {
        StoreStatusResponseDto deleteStore = storeService.deleteStoreRequest(storeId);

        return ResponseEntity.ok().body(ApiResponseData.success(deleteStore, "매장 삭제 요청이 접수되었습니다."));
    }

    // 3.5 매장 수정 요청
    @PutMapping("/stores/{storeId}")
    public ResponseEntity<ApiResponseData<StoreStatusResponseDto>> updateStoreRequest(@PathVariable UUID storeId,
                                                                     @RequestBody StoreUpdateRequestDto requestDto) {
        StoreStatusResponseDto updateStore = storeService.updateStoreRequest(storeId, requestDto);

        return ResponseEntity.ok().body(ApiResponseData.success(updateStore, "매장 정보 수정 요청이 접수되었습니다."));
    }
}
