package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.CreateStoreRequestDto;
import com.sparta.bedelivery.dto.StoreRequestDto;
import com.sparta.bedelivery.dto.StoreUpdateRequestDto;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.repository.StoreRepository;
import com.sparta.bedelivery.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class StoreServiceTest {

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StoreService storeService;

    @Test
    @DisplayName("매장 등록 요청하기")
    void createStoreRequest() {
        String userId = "test";
        String createBy = "TestUser";
        String userNameAndNickName = "TestUser";
        String password = "1234";
        String phone = "123456789";

        UUID locationCategoryId = UUID.fromString("c83851a5-ba66-409d-ad54-2c174789e43d");
        UUID industryCategoryId = UUID.fromString("ff5fd6b8-e594-455c-8270-cfb2a00ca9a2");

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

        storeService.createStoreRequest(storeRequestDto, userId);
    }

    @Test
    @DisplayName("관리자의 매장 등록 요청하기")
    void createStore() {
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
        String masterId = "master01";
        User masterUser = userRepository.findByUserId(masterId).orElse(null);
        assertEquals(User.Role.MASTER, masterUser.getRole(),
                "관리자 권한이 없습니다.");

        UUID locationCategoryId = UUID.fromString("193d17db-a3c3-4cf2-8390-e46c21105abd");
        UUID industryCategoryId = UUID.fromString("81c54a91-f1a6-4caf-94b9-2eb614e02ca2");

        CreateStoreRequestDto createStoreRequestDto = new CreateStoreRequestDto();
        createStoreRequestDto.setUserId(ownerId);  // OWNER 권한을 가진 유저의 ID
        createStoreRequestDto.setName("맛있는 치킨집");
        createStoreRequestDto.setLocationCategoryId(locationCategoryId);
        createStoreRequestDto.setIndustryCategoryId(industryCategoryId);
        createStoreRequestDto.setAddress("수원시 어딘가");
        createStoreRequestDto.setPhone("03132323");
        createStoreRequestDto.setImageUrl("example.jpg");
        createStoreRequestDto.setStatus(Store.Status.OPEN);

        storeService.createStore(createStoreRequestDto);
    }

    @Test
    @DisplayName("관리자의 매장 등록 승인")
    void approveStore() {
        UUID storeId = UUID.fromString("63ff7ab5-cabc-4381-8164-645bd2022482");
        Store store = storeRepository.findById(storeId).orElse(null);
        assertNotNull(store, "찾으시는 매장이 없습니다.");

        storeService.approveStore(storeId);
    }

    @Test
    @DisplayName("Open 상태인 매장 찾기")
    void findOpenStores() {

        List<Store> stores = storeRepository.findByStatus(Store.Status.OPEN);
        assertFalse(stores.isEmpty(), "영업중인 매장이 없습니다.");
        storeService.findOpenStores();
    }

    @Test
    @DisplayName("매장 상세 정보 찾기 (메뉴와 리뷰)")
    void getAllStores() {
        UUID storeId = UUID.fromString("1fa2514e-3db6-4f81-9149-43b55dde8bf9");
        Store store = storeRepository.findById(storeId).orElse(null);
        storeService.getAllStores(storeId);
        assertNotNull(storeId, "찾으시는 매장이 없습니다.");

        assertNotEquals(store.getStatus(), Store.Status.DELETE, "영업중인 매장이 아닙니다.");
        assertNotEquals(store.getStatus(), Store.Status.DELETE_REQUESTED, "영업중인 매장이 아닙니다.");

    }

    @Nested
    @DisplayName("매장 삭제 테스트")
    class deleteStoreTest {
        @Test
        @Order(1)
        @DisplayName("가게 주인의 매장 삭제 요청")
        void deleteStoreRequest() {
            UUID storeId = UUID.fromString("e6cce9f0-7ab8-48ec-8dbe-081cd7d94097");

            Store store = storeRepository.findById(storeId).orElse(null);
            storeService.deleteStoreRequest(storeId);
            assertNotNull(store, "찾으시는 매장이 없습니다.");
            assertNotEquals(store.getStatus() == Store.Status.DELETE,
                    "이미 삭제처리 된 매장입니다.");
        }

        @Test
        @Order(2)
        @DisplayName("관리자의 매장 삭제 승인")
        void approveStore1() {
            UUID storeId = UUID.fromString("e6cce9f0-7ab8-48ec-8dbe-081cd7d94097");

            Store store = storeRepository.findById(storeId).orElse(null);
            assertNotNull(store, "찾으시는 매장이 없습니다.");
            storeService.approveStore(storeId);
            assertFalse(store.getStatus() == Store.Status.DELETE_REQUESTED,
                    "삭제를 요청한 매장이 아닙니다.");
        }
    }

    @Test
    @DisplayName("가게 주인의 매장 수정 요청")
    void updateStoreRequest() {
        UUID storeId = UUID.fromString("e6cce9f0-7ab8-48ec-8dbe-081cd7d94097");

        Store store = storeRepository.findById(storeId).orElse(null);

        assertNotEquals(store.getStatus() == Store.Status.PENDING,
                "매장 수정을 요청할 수 없습니다.");
        StoreUpdateRequestDto storeUpdateRequest = new StoreUpdateRequestDto();
        storeUpdateRequest.setName("맛있고 친절한 치킨집");
        storeUpdateRequest.setAddress("서울시 어딘가");
        storeUpdateRequest.setPhone("02020202020");
        storeUpdateRequest.setImageUrl("example.jpg");

        storeService.updateStoreRequest(storeId, storeUpdateRequest);
        store.setStatus(Store.Status.UPDATE_REQUESTED);
    }

    @Test
    @DisplayName("관리자의 매장 수정 승인")
    void approveStore2() {
        UUID storeId = UUID.fromString("e6cce9f0-7ab8-48ec-8dbe-081cd7d94097");

        Store store = storeRepository.findById(storeId).orElse(null);
        assertNotNull(store, "찾으시는 매장이 없습니다.");
        storeService.approveStore(storeId);
        assertFalse(store.getStatus() == Store.Status.UPDATE_REQUESTED,
                "수정을 요청한 매장이 아닙니다.");
    }

    @Test
    @DisplayName("관리자용 전체 매장 목록 조회")
    void findAllStores() {
        User user = userRepository.findByUserId("master01")
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        assertTrue(user.getRole() == User.Role.MASTER,
                "MASTER 권한을 가진 사용자가 아닙니다.");
        storeService.findAllStores(user.getUserId());
    }
}