package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.*;
import com.sparta.bedelivery.repository.*;
import jakarta.persistence.EntityNotFoundException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.lang.String.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    private final IndustryCategoryRepository industryCategoryRepository;

    private final LocationCategoryRepository locationCategoryRepository;

    private final StoreIndustryCategoryRepository storeIndustryCategoryRepository;

    private final UserRepository userRepository;

    private final StoreUpdateRequestRepository storeUpdateRequestRepository;

    private final ReviewService reviewService;
    @Transactional
    public StoreResponseDto createStoreRequest(StoreRequestDto requestDto, String userId) {
        // 1. IndustryCategory 조회
        IndustryCategory industryCategory = industryCategoryRepository.findById(requestDto.getIndustryCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Industry category not found with ID: " + requestDto.getIndustryCategoryId()));
        // 2. LocationCategory 조회
        LocationCategory locationCategory = locationCategoryRepository.findById(requestDto.getLocationCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Location category not found with ID: " + requestDto.getLocationCategoryId()));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserId not found"));

        if (user.getRole() != User.Role.OWNER){ // 토큰 값으로만 등록이 가능해서 추가 수정
            throw new IllegalArgumentException("매장을 등록할 권한이 필요합니다.");
        }
        Store savedStore = Store.builder()
                .userId(user)
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .locationCategory(locationCategory)
                .storeIndustryCategories(new ArrayList<>())
                .phone(requestDto.getPhone())
                .imageUrl(requestDto.getImageUrl())
                .status(Store.Status.PENDING)
                .build();

        storeRepository.save(savedStore);

        StoreIndustryCategory storeIndustryCategory = new StoreIndustryCategory();
        storeIndustryCategory.setStore(savedStore);
        storeIndustryCategory.setIndustryCategory(industryCategory);
        savedStore.getStoreIndustryCategories().add(storeIndustryCategory);
        storeIndustryCategoryRepository.save(storeIndustryCategory);

        Store refreshedStore = storeRepository.findById(savedStore.getId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        return new StoreResponseDto(refreshedStore,null);
    }

    public List<StoreResponseDto> findOpenStores() {

        List<Store> store = storeRepository.findByStatus(Store.Status.OPEN);

        return store.stream()
                .map(eachStore -> {
                    return new StoreResponseDto(eachStore, reviewService.getStoreReviewInfo(eachStore.getId())); // 3️⃣ Store + Redis 데이터 함께 반환
                })
                .toList();
    }
//    @Transactional(readOnly = true)
    public List<StoreDetailsResponseDto> getAllStores(UUID storeId) {

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));
        if (store.getStatus() != Store.Status.DELETE && store.getStatus() != Store.Status.DELETE_REQUESTED) {
            return List.of(new StoreDetailsResponseDto(store));
        }
        throw new EntityNotFoundException("영업 중인 매장이 없습니다.");
    }
    @Transactional
    public StoreStatusResponseDto deleteStoreRequest(UUID storeId) {
        Store status = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + storeId));

        if (status.getStatus() == Store.Status.DELETE) {
            throw new EntityNotFoundException("Store is deleted");
        }
        status.delete("MASTER");
        status.setStatus(Store.Status.DELETE_REQUESTED);
        storeRepository.save(status);

        return new StoreStatusResponseDto(status);
    }

    @Transactional
    public StoreStatusResponseDto updateStoreRequest(UUID storeId, StoreUpdateRequestDto requestDto) {
        Store status = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + storeId));

        List<StoreUpdateRequest> updateId = storeUpdateRequestRepository.findByStore_Id(storeId);

        if (status.getStatus() == Store.Status.PENDING) {
            if (updateId.isEmpty()) {
                StoreUpdateRequest updateRequest = StoreUpdateRequest.builder()
                        .storeId(status)
                        .name(requestDto.getName())
                        .address(requestDto.getAddress())
                        .phone(requestDto.getPhone())
                        .imageUrl(requestDto.getImageUrl())
                        .build();
                storeUpdateRequestRepository.save(updateRequest);
            }

            status.setStatus(Store.Status.UPDATE_REQUESTED);

            storeRepository.save(status);

            return new StoreStatusResponseDto(status);
        }
        throw new EntityNotFoundException("해당 매장은 오픈 준비중인 매장이 아닙니다.");
    }


    // 전체 매장 목록 조회 (관리자용)
    public List<StoreStatusResponseDto> findAllStores(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()->new IllegalArgumentException("유저를 찾을 수 없습니다."));
        if(user.getRole()!= User.Role.MASTER){
            throw new IllegalArgumentException("권한이 없습니다.");
        }

        List<Store> store = storeRepository.findAll();

        List<StoreStatusResponseDto> responseDtoList = store.stream()
                .map(StoreStatusResponseDto::new)
                .toList();

        return responseDtoList;
    }

    @Transactional
    public void deleteStore(UUID storeId) {
        Store store_id = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + storeId));
        if (store_id.getStatus() != Store.Status.DELETE) {
            throw new RuntimeException("이미 삭제된 매장입니다.");
        }
        StoreRequestDto requestDto = new StoreRequestDto();
        requestDto.setName(store_id.getName());
        requestDto.setAddress(store_id.getAddress());
        requestDto.setPhone(store_id.getPhone());
        requestDto.setImageUrl(store_id.getImageUrl());

        store_id.delete(String.valueOf(User.Role.MASTER));
        store_id.setStatus(Store.Status.DELETE);
        store_id.delete("MASTER");

        storeRepository.save(store_id);
    }

    // 매장 등록, 삭제 승인
    @Transactional
    public StoreStatusResponseDto approveStore(UUID storeId) {
        Store current_status = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with ID: " + storeId));

        Store.Status currentStatus = current_status.getStatus(); // 현재 상태 확인
        // 매장 등록 승인
        if (Store.Status.PENDING.equals(currentStatus)) {
            current_status.setCreateBy(valueOf(User.Role.MASTER));
            current_status.setStatus(Store.Status.OPEN);
        }
        // 매장 삭제 승인
        else if (Store.Status.DELETE_REQUESTED.equals(currentStatus)) {
            current_status.delete(String.valueOf(User.Role.MASTER));// 매장을 삭제한 deleteBy 가 저장이 안되는 사항 수정해야 함
            current_status.setStatus(Store.Status.DELETE);
        }
        // 유효하지 않은 상태
        else {
            throw new IllegalArgumentException("유효하지 않은 매장 상태입니다. 현재 상태: " + currentStatus);
        }

        storeRepository.save(current_status);

        return new StoreStatusResponseDto(current_status);
    }

    @Transactional
    public StoreStatusResponseDto updateStore(UUID storeId) {
        List<StoreUpdateRequest> updateRequests = storeUpdateRequestRepository.findByStore_IdOrderByCreateAtDesc(storeId);

        if (updateRequests.isEmpty()) {
            throw new IllegalArgumentException("수정 요청을 시도한 매장이 존재하지 않습니다.");
        }
        StoreUpdateRequest updateRequest = updateRequests.get(0); // 최신 요청만 처리
        Store updateStore = updateRequest.getStore();

        updateStore.update(
                updateRequest.getName(), updateRequest.getAddress(),
                updateRequest.getPhone(), updateRequest.getImageUrl()
        );
        updateRequest.setStatus(Store.Status.COMPLETED);
        updateStore.setStatus(Store.Status.UPDATED);

        storeRepository.save(updateStore);

        return new StoreStatusResponseDto(updateStore);
    }

    @Transactional
    public CreateStoreResponseDto createStore(CreateStoreRequestDto requestDto) {
        // 1. IndustryCategory 조회
        IndustryCategory industryCategory = industryCategoryRepository.findById(requestDto.getIndustryCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Industry category not found with ID: " + requestDto.getIndustryCategoryId()));

        // 2. LocationCategory 조회
        LocationCategory locationCategory = locationCategoryRepository.findById(requestDto.getLocationCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Location category not found with ID: " + requestDto.getLocationCategoryId()));

        // 3. User 조회 및 OWNER 권한 체크
        User owner = userRepository.findByUserId(requestDto.getUserId())
                .orElseThrow(()-> new IllegalArgumentException("권한을 찾지 못했습니다." + requestDto.getUserId()));

        if (!"OWNER".equals(owner.getRole().toString())) {
            throw new IllegalArgumentException("매장 등록은 OWNER 권한을 가진 사용자에게만 가능합니다.");
        }
        // 4. Store 생성
        Store store = Store.builder()
                .userId(owner)  // userId 대신 user 객체 전달
                .name(requestDto.getName())
                .locationCategory(locationCategory)
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .imageUrl(requestDto.getImageUrl())
                .storeIndustryCategories(new ArrayList<>())
                .status(requestDto.getStatus())
                .build();

        Store savedStore = storeRepository.save(store);

        StoreIndustryCategory storeIndustryCategory = new StoreIndustryCategory();
        storeIndustryCategory.setStore(savedStore);
        storeIndustryCategory.setIndustryCategory(industryCategory);
        savedStore.getStoreIndustryCategories().add(storeIndustryCategory);
        storeIndustryCategoryRepository.save(storeIndustryCategory);

        Store refreshedStore = storeRepository.findById(savedStore.getId())
                .orElseThrow(() -> new EntityNotFoundException("Store not found"));

        return new CreateStoreResponseDto(refreshedStore);
    }
}
