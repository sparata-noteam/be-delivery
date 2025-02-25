package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.store.*;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "매장")
public class StoreController {

    private final StoreService storeService;

    // 3.1 매장 등록 요청
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/stores/{userId}")
    @Operation(summary = "매장 등록 요청", description = "매장 주인 본인이 매장 등록을 요청합니다.")
    public ResponseEntity<ApiResponseData<StoreResponseDto>> createStoreRequest(@RequestBody StoreRequestDto requestDto
    , @PathVariable String userId) {  // UserDetails 대신 User 엔티티를 직접 받음
        StoreResponseDto newStore = storeService.createStoreRequest(requestDto, userId);

        return ResponseEntity.ok().body(ApiResponseData.success(newStore, "매장 등록 요청이 접수되었습니다."));
    }

    // 3.2 삭제 상태의 매장 제외 전체 매장 목록 조회 가능.
    @PreAuthorize("hasAnyRole('CUSTOMER','OWNER','MANAGER')")
    @GetMapping("/stores")
    @Operation(summary = "매장 조회", description = "매장을 조회하며, 선택적으로 업종과 지역으로 필터링할 수 있습니다.")
    public ResponseEntity<ApiResponseData<List<StoreResponseDto>>> getStores(
            @RequestParam(required = false) String industryName,
            @RequestParam(required = false) String locationName) {

        List<StoreResponseDto> stores = storeService.getStores(industryName, locationName);
        return ResponseEntity.ok().body(ApiResponseData.success(stores));
    }

    // 3.3 매장 상세 조회
    @GetMapping("/stores/{storeId}")
    @Operation(summary = "특정 매장 상세 조회", description = "매장 정보와 메뉴 목록을 조회합니다.")
    public ResponseEntity<ApiResponseData<List<StoreDetailsResponseDto>>> getAllStores(@PathVariable UUID storeId){
        List<StoreDetailsResponseDto> findAll = storeService.getAllStores(storeId);

        return ResponseEntity.ok().body(ApiResponseData.success(findAll));
    }

    // 3.4 매장 삭제 요청
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/stores/{storeId}")
    @Operation(summary = "매장 삭제 요청", description = "매장 주인 본인이 매장 삭제를 요청합니다.")
    public ResponseEntity<ApiResponseData<StoreStatusResponseDto>> deleteStoreRequest(@PathVariable UUID storeId) {
        StoreStatusResponseDto deleteStore = storeService.deleteStoreRequest(storeId);

        return ResponseEntity.ok().body(ApiResponseData.success(deleteStore, "매장 삭제 요청이 접수되었습니다."));
    }

    // 3.5 매장 수정 요청
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/stores/{storeId}")
    @Operation(summary = "매장 수정 요청", description = "매장 주인 본인이 매장 수정을 요청합니다.")
    public ResponseEntity<ApiResponseData<StoreStatusResponseDto>> updateStoreRequest(@PathVariable UUID storeId,
                                                                     @RequestBody StoreUpdateRequestDto requestDto) {
        StoreStatusResponseDto updateStore = storeService.updateStoreRequest(storeId, requestDto);

        return ResponseEntity.ok().body(ApiResponseData.success(updateStore, "매장 정보 수정 요청이 접수되었습니다."));
    }
}
