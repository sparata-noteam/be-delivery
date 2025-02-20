package com.sparta.bedelivery.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.dto.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.CreateMenuResponseDto;
import com.sparta.bedelivery.repository.MenuImageRepository;
import com.sparta.bedelivery.repository.MenuRepository;
import com.sparta.bedelivery.repository.StoreRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 테스트 인스턴스의 생성 단위를 클래스로 변경
@Transactional
class MenuServiceTest {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuImageRepository menuImageRepository;
    @Autowired
    private MenuService menuService;

    ObjectMapper objectMapper;

    @Test
    @DisplayName("메뉴 등록 하기")
    void createMenu() {

        UUID storeId = UUID.fromString("633f5edf-9602-4cf8-b5fd-e8b0dfacf175");

        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
        requestDto.setStoreId(storeId);
        requestDto.setName("메뉴 이름");
        requestDto.setPrice(BigDecimal.valueOf(18000));
        requestDto.setDescription("메뉴 설명");
        requestDto.setIsHidden(false);
        CreateMenuResponseDto responseDto = menuService.createMenu(requestDto);

        System.out.printf("responseDto: %s\n", responseDto);
    }
}