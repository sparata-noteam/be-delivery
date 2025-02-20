//package com.sparta.bedelivery.service;
//
//import com.sparta.bedelivery.BeDeliveryApplication;
//import com.sparta.bedelivery.dto.CreateMenuRequestDto;
//import com.sparta.bedelivery.dto.CreateMenuResponseDto;
//import com.sparta.bedelivery.entity.Menu;
//import com.sparta.bedelivery.entity.Store;
//import com.sparta.bedelivery.repository.MenuRepository;
//import com.sparta.bedelivery.repository.StoreRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Slf4j
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@SpringBootTest(classes = BeDeliveryApplication.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Transactional
//class MenuServiceTest {
//
//    @Autowired
//    private MenuService menuService;
//    @Autowired
//    private MenuRepository menuRepository;
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @Test
//    @DisplayName("메뉴 등록 테스트")
//    void createMenu() {
//        // Arrange
//        UUID storeId = UUID.fromString("633f5edf-9602-4cf8-b5fd-e8b0dfacf175");
//
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
//        requestDto.setStoreId(storeId);
//        requestDto.setName("새로운 메뉴");
//        requestDto.setPrice(BigDecimal.valueOf(18000));
//        requestDto.setDescription("맛있는 메뉴 설명");
//        requestDto.setIsHidden(false);
//
//        // Act
//        CreateMenuResponseDto responseDto = menuService.createMenu(requestDto);
//
//        // Assert
//        assertNotNull(responseDto);
//        assertEquals("새로운 메뉴", responseDto.getName());
//        assertEquals(BigDecimal.valueOf(18000), responseDto.getPrice());
//        assertEquals("맛있는 메뉴 설명", responseDto.getDescription());
//
//        // 메뉴가 실제로 저장되었는지 확인
//        Menu menu = menuRepository.findById(responseDto.getMenuId()).orElseThrow();
//        assertNotNull(menu);
//        assertEquals(store, menu.getStore());
//    }
//
//    @Test
//    @DisplayName("모든 메뉴 조회 테스트")
//    void findAllMenus() {
//        // Arrange
//        UUID storeId = UUID.fromString("633f5edf-9602-4cf8-b5fd-e8b0dfacf175");
//
//        // 메뉴가 2개 이상 있어야 테스트 가능
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        CreateMenuRequestDto requestDto1 = new CreateMenuRequestDto();
//        requestDto1.setStoreId(storeId);
//        requestDto1.setName("메뉴 1");
//        requestDto1.setPrice(BigDecimal.valueOf(10000));
//        requestDto1.setDescription("메뉴 1 설명");
//        requestDto1.setIsHidden(false);
//
//        CreateMenuRequestDto requestDto2 = new CreateMenuRequestDto();
//        requestDto2.setStoreId(storeId);
//        requestDto2.setName("메뉴 2");
//        requestDto2.setPrice(BigDecimal.valueOf(12000));
//        requestDto2.setDescription("메뉴 2 설명");
//        requestDto2.setIsHidden(true);  // 숨김 처리된 메뉴
//
//        menuService.createMenu(requestDto1);
//        menuService.createMenu(requestDto2);
//
//        // Act
//        List<CreateMenuResponseDto> menus = menuService.findAllMenus(storeId);
//
//        // Assert
//        assertEquals(1, menus.size()); // 숨김 메뉴는 제외되고 1개만 조회돼야 한다.
//    }
//
//    @Test
//    @DisplayName("메뉴 조회 테스트")
//    void findMenu() {
//        // Arrange
//        UUID storeId = UUID.fromString("633f5edf-9602-4cf8-b5fd-e8b0dfacf175");
//
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
//        requestDto.setStoreId(storeId);
//        requestDto.setName("조회할 메뉴");
//        requestDto.setPrice(BigDecimal.valueOf(15000));
//        requestDto.setDescription("조회할 메뉴 설명");
//        requestDto.setIsHidden(false);
//
//        CreateMenuResponseDto responseDto = menuService.createMenu(requestDto);
//
//        // Act
//        CreateMenuResponseDto foundMenu = menuService.findMenus(responseDto.getMenuId());
//
//        // Assert
//        assertNotNull(foundMenu);
//        assertEquals(responseDto.getMenuId(), foundMenu.getMenuId());
//    }
//
//    @Test
//    @DisplayName("메뉴 업데이트 테스트")
//    void updateMenu() {
//        // Arrange
//        UUID storeId = UUID.fromString("633f5edf-9602-4cf8-b5fd-e8b0dfacf175");
//
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
//        requestDto.setStoreId(storeId);
//        requestDto.setName("기존 메뉴");
//        requestDto.setPrice(BigDecimal.valueOf(15000));
//        requestDto.setDescription("기존 메뉴 설명");
//        requestDto.setIsHidden(false);
//
//        CreateMenuResponseDto responseDto = menuService.createMenu(requestDto);
//
//        // 새로운 값으로 업데이트할 Dto 생성
//        CreateMenuRequestDto updatedDto = new CreateMenuRequestDto();
//        updatedDto.setStoreId(storeId);
//        updatedDto.setName("업데이트된 메뉴");
//        updatedDto.setPrice(BigDecimal.valueOf(20000));
//        updatedDto.setDescription("업데이트된 설명");
//        updatedDto.setIsHidden(true);
//
//        // Act
//        CreateMenuResponseDto updatedMenu = menuService.updateMenu(responseDto.getMenuId(), updatedDto);
//
//        // Assert
//        assertEquals("업데이트된 메뉴", updatedMenu.getName());
//        assertEquals(BigDecimal.valueOf(20000), updatedMenu.getPrice());
//        assertEquals("업데이트된 설명", updatedMenu.getDescription());
//        assertTrue(updatedMenu.getIsHidden());
//    }
//
//    @Test
//    @DisplayName("메뉴 삭제 테스트")
//    void deleteMenu() {
//        // Arrange
//        UUID storeId = UUID.fromString("633f5edf-9602-4cf8-b5fd-e8b0dfacf175");
//
//        Store store = storeRepository.findById(storeId).orElseThrow();
//        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
//        requestDto.setStoreId(storeId);
//        requestDto.setName("삭제할 메뉴");
//        requestDto.setPrice(BigDecimal.valueOf(20000));
//        requestDto.setDescription("삭제할 메뉴 설명");
//        requestDto.setIsHidden(false);
//
//        CreateMenuResponseDto responseDto = menuService.createMenu(requestDto);
//
//        // Act
//        menuService.deleteMenu(responseDto.getMenuId());
//
//        // Assert
//        Menu deletedMenu = menuRepository.findById(responseDto.getStoreId()).orElseThrow();
//        assertTrue(deletedMenu.getIsHidden()); // 삭제된 메뉴는 숨김 처리되어야 한다.
//    }
//}
