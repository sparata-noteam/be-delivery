package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.CreateMenuResponseDto;
import com.sparta.bedelivery.entity.Menu;
import com.sparta.bedelivery.repository.MenuRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 등록 테스트")
    void createMenuTest() {
        String address = "서울 강남구";

        assertNotNull(address, "매장이 존재해야 합니다.");

        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
        requestDto.setStoreAddress(address);
        requestDto.setName("메뉴 이름");
        requestDto.setPrice(BigDecimal.valueOf(18000));
        requestDto.setDescription("메뉴 설명");
        requestDto.setIsHidden(false);
        requestDto.setImageUrl(Arrays.asList("image1.jpg", "image2.jpg"));

        menuService.createMenu(requestDto);

    }

    @Test
    @DisplayName("전체 메뉴 조회 테스트")
    void findAllMenus() {
        // given
        UUID storeId = UUID.fromString("ae3e2e33-fb9c-49cd-90d1-d0321bdd9233");
        // when
        List<CreateMenuResponseDto> menus = menuService.findAllMenus(storeId);
        // then
        assertNotNull(menus, "메뉴 리스트는 null이 아니어야 합니다");
        assertFalse(menus.isEmpty(), "메뉴 리스트는 비어있지 않아야 합니다");

        // 숨겨진 메뉴가 포함되어 있지 않은지 확인
        boolean hasHiddenMenu = menus.stream()
                .anyMatch(CreateMenuResponseDto::getIsHidden);
        assertFalse(hasHiddenMenu, "숨겨진 메뉴는 포함되지 않아야 합니다");
    }

    @Test
    @DisplayName("특정 메뉴 조회 테스트")
    void findMenu() {
        UUID menuId = UUID.fromString("f3516601-04d7-4c64-a175-f82d057bc5b0");
        Menu menu = menuRepository.findById(menuId).orElseThrow(null);
        assertEquals(menuId, menu.getId(),
                "해당 메뉴는 없습니다.");
        assertTrue(!menu.getIsHidden(), "해당 메뉴는 존재하지 않습니다.");
    }

    @Test
    @DisplayName("메뉴 수정하기 테스트")
    void updateMenu() {
        String address = "서울 강남구";
        UUID menuId = UUID.fromString("f3516601-04d7-4c64-a175-f82d057bc5b0");
        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
        requestDto.setStoreAddress(address);
        requestDto.setName("메뉴 이름");
        requestDto.setPrice(BigDecimal.valueOf(18000));
        requestDto.setDescription("메뉴 설명");
        requestDto.setIsHidden(false);
        requestDto.setImageUrl(Arrays.asList("image1.jpg", "image2.jpg"));
        // when
        CreateMenuResponseDto responseDto = menuService.updateMenu(menuId, requestDto);
        // then
        assertNotNull(responseDto, "응답이 null이 아니어야 합니다.");
        assertEquals(requestDto.getName(), responseDto.getName(), "메뉴 이름이 일치해야 합니다.");
        assertEquals(requestDto.getPrice(), responseDto.getPrice(), "가격이 일치해야 합니다.");
        assertEquals(requestDto.getDescription(), responseDto.getDescription(), "설명이 일치해야 합니다.");

        // 이미지 검증
        assertNotNull(responseDto.getImageUrl(), "이미지 리스트가 null이 아니어야 합니다.");
        assertFalse(responseDto.getImageUrl().isEmpty(), "이미지 리스트가 비어있지 않아야 합니다.");

    }

    @Test
    @DisplayName("메뉴 삭제 테스트")
    void deleteMenu() {
        UUID menuId = UUID.fromString("f3516601-04d7-4c64-a175-f82d057bc5b0");
        Menu menu = menuRepository.findById(menuId).orElseThrow(null);

        assertTrue(menu.getId() == menuId, "메뉴를 찾을 수 없습니다.");
        menuService.deleteMenu(menuId);
    }
}