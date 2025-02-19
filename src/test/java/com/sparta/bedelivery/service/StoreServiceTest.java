package com.sparta.bedelivery.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sparta.bedelivery.dto.*;
import com.sparta.bedelivery.entity.*;
import com.sparta.bedelivery.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class StoreServiceTest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private IndustryCategoryRepository industryCategoryRepository;
    @Autowired
    private LocationCategoryRepository locationCategoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreUpdateRequestRepository storeUpdateRequestRepository;

    private ObjectMapper objectMapper;

    @BeforeAll
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @DisplayName("매장 등록 요청하기")
    void createStoreRequest() throws JsonProcessingException {
        String userId = "test";
        String createBy = "TestUser";
        String userNameAndNickName = "TestUser";
        String password = "1234";
        String phone = "123456789";

        UUID locationCategoryId = UUID.fromString("a6dad1bb-3d7b-46ca-891a-7d9c906d07e5");
        UUID industryCategoryId = UUID.fromString("d9453d44-daa9-49c9-b7ab-6c4e2de4b475");

        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setName(userNameAndNickName);
        mockUser.setNickname(userNameAndNickName);
        mockUser.setPassword(password);
        mockUser.setPhone(phone);
        mockUser.setRole(User.Role.OWNER);
        mockUser.setCreateBy(createBy);
        mockUser.setUpdateBy(createBy);
        userRepository.save(mockUser);

        StoreRequestDto storeRequestDto = new StoreRequestDto();
        storeRequestDto.setName("맛있는 치킨집");
        storeRequestDto.setLocationCategoryId(locationCategoryId);
        storeRequestDto.setIndustryCategoryId(industryCategoryId);
        storeRequestDto.setAddress("수원시 어딘가");
        storeRequestDto.setPhone("03132323");
        storeRequestDto.setImageUrl("example.jpg");

        StoreResponseDto result = storeService.createStoreRequest(storeRequestDto, userId);

        IndustryCategory mockIndustryCategory = new IndustryCategory();
        mockIndustryCategory.setId(industryCategoryId);
        industryCategoryRepository.save(mockIndustryCategory);

        LocationCategory mockLocationCategory = new LocationCategory();
        mockLocationCategory.setId(locationCategoryId);
        locationCategoryRepository.save(mockLocationCategory);

        String json = objectMapper.writeValueAsString(result);
        log.info("매장 등록 요청 완료: result = {}", json);
    }

    @Test
    @DisplayName("관리자의 매장 등록 요청하기")
    void createStore() throws JsonProcessingException {
        // 매장을 등록할 OWNER 유저 설정
        String ownerId = "test";
        User ownerUser = new User();
        ownerUser.setUserId(ownerId);
        ownerUser.setName("TestOwner");
        ownerUser.setNickname("TestOwner");
        ownerUser.setPassword("1234");
        ownerUser.setPhone("123456789");
        ownerUser.setRole(User.Role.OWNER);
        ownerUser.setCreateBy("MASTER");
        ownerUser.setUpdateBy("MASTER");
        userRepository.save(ownerUser);

        // 마스터 권한 체크
        String masterId = "master";
        User masterUser = userRepository.findByUserId(masterId).orElse(null);
        assertEquals(User.Role.MASTER, masterUser.getRole(),
                "관리자 권한이 없습니다.");

        UUID locationCategoryId = UUID.fromString("a6dad1bb-3d7b-46ca-891a-7d9c906d07e5");
        UUID industryCategoryId = UUID.fromString("d9453d44-daa9-49c9-b7ab-6c4e2de4b475");

        CreateStoreRequestDto createStoreRequestDto = new CreateStoreRequestDto();
        createStoreRequestDto.setUserId(ownerId);  // OWNER 권한을 가진 유저의 ID
        createStoreRequestDto.setName("맛있는 치킨집");
        createStoreRequestDto.setLocationCategoryId(locationCategoryId);
        createStoreRequestDto.setIndustryCategoryId(industryCategoryId);
        createStoreRequestDto.setAddress("수원시 어딘가");
        createStoreRequestDto.setPhone("03132323");
        createStoreRequestDto.setImageUrl("example.jpg");
        createStoreRequestDto.setStatus(Store.Status.OPEN);

        CreateStoreResponseDto result = storeService.createStore(createStoreRequestDto);

        IndustryCategory mockIndustryCategory = new IndustryCategory();
        mockIndustryCategory.setId(industryCategoryId);
        industryCategoryRepository.save(mockIndustryCategory);

        LocationCategory mockLocationCategory = new LocationCategory();
        mockLocationCategory.setId(locationCategoryId);
        locationCategoryRepository.save(mockLocationCategory);

        String json = objectMapper.writeValueAsString(result);
        log.info("매장 등록 요청 완료: result = {}", json);
    }

    @Test
    @DisplayName("관리자의 매장 등록 승인")
    void approveStore() throws JsonProcessingException {
        UUID storeId = UUID.fromString("633f5edf-9602-4cf8-b5fd-e8b0dfacf175");
        Store store = storeRepository.findById(storeId).orElse(null);
        assertNotNull(store, "찾으시는 매장이 없습니다.");

        assertTrue(store.getStatus() == Store.Status.PENDING,
                "등록을 요청한 매장이 아닙니다.");
        store.setStatus(Store.Status.OPEN);
        StoreStatusResponseDto storeStatusResponseDto = new StoreStatusResponseDto(store);

        String json = objectMapper.writeValueAsString(storeStatusResponseDto);
        log.info("매장 등록 요청 정보 : " + json);
    }

    @Test
    @DisplayName("Open 상태인 매장 찾기")
    void findOpenStores() throws JsonProcessingException {
        log.info("Open 상태인 매장 가져오기");

        List<Store> stores = storeRepository.findByStatus(Store.Status.OPEN);

        for (Store store : stores) {
            assertEquals(Store.Status.OPEN, store.getStatus(),
                    "Open 상태의 매장만 확인할 수 있습니다.");
        }

        List<StoreResponseDto> storeResponseDtos = stores.stream()
                .map(StoreResponseDto::new).toList();

        String json = objectMapper.writeValueAsString(storeResponseDtos);
        log.info("Open 상태인 매장 리스트 : " + json);

        assertFalse(stores.isEmpty(), "영업중인 매장이 없습니다.");
    }

    @Test
    @DisplayName("매장 상세 정보 찾기 (메뉴와 리뷰)")
    void getAllStores() throws JsonProcessingException {
        UUID storeId = UUID.fromString("22324acb-c520-4b4f-9140-38a0f798f57a");

        Store store = storeRepository.findById(storeId).orElse(null);

        assertNotNull(store, "찾으시는 매장이 없습니다.");

        assertNotEquals(store.getStatus(), Store.Status.DELETE, "영업중인 매장이 아닙니다.");
        assertNotEquals(store.getStatus(), Store.Status.DELETE_REQUESTED, "영업중인 매장이 아닙니다.");

        List<StoreDetailsResponseDto> storeDetailsResponseDtos = new ArrayList<>();
        storeDetailsResponseDtos.add(new StoreDetailsResponseDto(store));

        String json = objectMapper.writeValueAsString(storeDetailsResponseDtos);
        log.info("매장 상세 정보 : " + json);
    }

    @Nested
    @DisplayName("매장 삭제 테스트")
    class deleteStoreTest {
        @Test
        @Order(1)
        @DisplayName("가게 주인의 매장 삭제 요청")
        void deleteStoreRequest() throws JsonProcessingException {
            UUID storeId = UUID.fromString("323f7c29-5e11-4bd9-8231-73a6faf36397");

            Store store = storeRepository.findById(storeId).orElse(null);

            assertNotNull(store, "찾으시는 매장이 없습니다.");
            assertNotEquals(store.getStatus() == Store.Status.DELETE,
                    "이미 삭제처리 된 매장입니다.");

            store.setStatus(Store.Status.DELETE_REQUESTED);
            StoreStatusResponseDto storeStatusResponseDto = new StoreStatusResponseDto(store);

            String json = objectMapper.writeValueAsString(storeStatusResponseDto);
            log.info("매장 삭제 요청 정보 : " + json);
        }

        @Test
        @Order(2)
        @DisplayName("관리자의 매장 삭제 승인")
        void approveStore1() throws JsonProcessingException {
            UUID storeId = UUID.fromString("323f7c29-5e11-4bd9-8231-73a6faf36397");

            Store store = storeRepository.findById(storeId).orElse(null);
            assertNotNull(store, "찾으시는 매장이 없습니다.");

            assertFalse(store.getStatus() == Store.Status.DELETE_REQUESTED,
                    "삭제를 요청한 매장이 아닙니다.");
            store.setStatus(Store.Status.DELETE);
            StoreStatusResponseDto storeStatusResponseDto = new StoreStatusResponseDto(store);

            String json = objectMapper.writeValueAsString(storeStatusResponseDto);
            log.info("매장 삭제 요청 정보 : " + json);
        }
    }

    @Test
    @DisplayName("가게 주인의 매장 수정 요청")
    void updateStoreRequest() throws JsonProcessingException {
        UUID storeId = UUID.fromString("633f5edf-9602-4cf8-b5fd-e8b0dfacf175");

        Store store = storeRepository.findById(storeId).orElse(null);

        List<StoreUpdateRequest> updateRequests = storeUpdateRequestRepository.findByStore_Id(storeId);

        assertNotEquals(store.getStatus() == Store.Status.PENDING,
                "매장 수정을 요청할 수 없습니다.");
        if (updateRequests.isEmpty()) {
            StoreUpdateRequest storeUpdateRequest = StoreUpdateRequest.builder()
                    .storeId(store)
                    .name("맛있고 친절한 치킨집")
                    .address("서울시 어딘가")
                    .phone("02020202020")
                    .imageUrl("example.jpg")
                    .build();
            storeUpdateRequestRepository.save(storeUpdateRequest);
        }
        store.setStatus(Store.Status.UPDATE_REQUESTED);
        StoreStatusResponseDto storeStatusResponseDto = new StoreStatusResponseDto(store);

        String json = objectMapper.writeValueAsString(storeStatusResponseDto);
        log.info("매장 수정 요청 정보 : " + json);
    }

    @Test
    @DisplayName("관리자의 매장 수정 승인")
    void approveStore2() throws JsonProcessingException {
        UUID storeId = UUID.fromString("4af35dd6-7d89-4fcd-adef-3305466c886d");
        Store store = storeRepository.findById(storeId).orElse(null);
        List<StoreUpdateRequest> updateRequests = storeUpdateRequestRepository.findByStore_IdOrderByCreateAtDesc(storeId);

        assertEquals(store.getStatus(), Store.Status.UPDATE_REQUESTED, "해당 매장의 수정 요청이 없습니다.");

        StoreUpdateRequest latest = updateRequests.get(0);
        Store updateStore = latest.getStore();

        updateStore.update(latest.getName(), latest.getAddress(),
                latest.getPhone(), latest.getImageUrl());

        latest.setStatus(Store.Status.COMPLETED);
        updateStore.setStatus(Store.Status.UPDATED);

        String json = objectMapper.writeValueAsString(new StoreStatusResponseDto(updateStore));
        log.info("매장 수정 승인 정보 : " + json);
    }

    @Test
    @DisplayName("관리자용 전체 매장 목록 조회")
    void findAllStores() throws JsonProcessingException {
        List<Store> stores = storeRepository.findAll();
        User user = userRepository.findByUserId("master")
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        assertTrue(user.getRole() == User.Role.MASTER,
                "MASTER 권한을 가진 사용자가 아닙니다.");

        List<StoreStatusResponseDto> storeStatusResponseDtos =
                stores.stream().map(StoreStatusResponseDto::new).toList();

        String json = objectMapper.writeValueAsString(storeStatusResponseDtos);
        log.info("매장 수정 요청 정보 : " + json);
    }
}