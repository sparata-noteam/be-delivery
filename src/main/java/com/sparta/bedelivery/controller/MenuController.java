package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.CreateMenuResponseDto;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 7.1 메뉴 등록
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("/menus")
    public ResponseEntity<ApiResponseData<CreateMenuResponseDto>> createMenu(@RequestBody CreateMenuRequestDto requestDto){

        CreateMenuResponseDto menu = menuService.createMenu(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseData.success(menu));
    }

    // 7.2 특정 매장의 메뉴 목록 조회
    @GetMapping("/menus")
    public ResponseEntity<ApiResponseData<List<CreateMenuResponseDto>>> findAllMenus(@RequestParam("storeId") UUID storeId) {
        // 매장 ID에 해당하는 메뉴 목록을 조회
        List<CreateMenuResponseDto> menuResponseDto = menuService.findAllMenus(storeId);

        // OK 응답 반환
        return ResponseEntity.ok().body(ApiResponseData.success(menuResponseDto));
    }

    // 7.3 메뉴 상세 조회
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponseData<CreateMenuResponseDto>> findMenus(@PathVariable UUID menuId) {
        // 매장 ID에 해당하는 메뉴 목록을 조회
        CreateMenuResponseDto findMenu = menuService.findMenus(menuId);

        // OK 응답 반환
        return ResponseEntity.ok().body(ApiResponseData.success(findMenu));
    }

    // 7.4 메뉴 수정
    @PreAuthorize("hasRole('OWNER')")
    @PutMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponseData<CreateMenuResponseDto>> updateMenu(@PathVariable UUID menuId,
                                                            @RequestBody CreateMenuRequestDto requestDto) {
        CreateMenuResponseDto menuUpdate = menuService.updateMenu(menuId, requestDto);

        return ResponseEntity.ok().body(ApiResponseData.success(menuUpdate));
    }

    // 7.5 메뉴 삭제
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<ApiResponseData<Void>> deleteMenu(@PathVariable UUID menuId) {
        menuService.deleteMenu(menuId);

        return ResponseEntity.ok().body(ApiResponseData.success(null, "메뉴가 삭제되었습니다."));
    }
}
