package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.CreateMenuResponseDto;
import com.sparta.bedelivery.repository.MenuImageRepository;
import com.sparta.bedelivery.repository.MenuRepository;
import com.sparta.bedelivery.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private StoreRepository storeRepository;
    @Mock
    private MenuImageRepository menuImageRepository;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 등록 하기")
    void createMenu() {

        UUID storeId = UUID.fromString("61f38e8e-7373-4009-825e-3b5a7b32cd80");

        CreateMenuRequestDto requestDto = new CreateMenuRequestDto();
        requestDto.setStoreId(storeId);
        requestDto.setName("메뉴 이름");
        requestDto.setPrice(BigDecimal.valueOf(18000));
        requestDto.setDescription("메뉴 설명");
        requestDto.setIsHidden(false);
        CreateMenuResponseDto responseDto = menuService.createMenu(requestDto);

//        given(storeRepository.findById(storeId)).willReturn(Optional.of());

        System.out.printf("responseDto: %s\n", responseDto);
    }
}