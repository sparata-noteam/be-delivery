package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.menu.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.menu.CreateMenuResponseDto;
import com.sparta.bedelivery.global.response.ApiResponseData;
import com.sparta.bedelivery.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "메뉴")
public class MenuController {

    private final MenuService menuService;

    // 7.1 메뉴 등록
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PostMapping("/menus")
    @Operation(summary = "매장 관리자의 메뉴 추가", description = "사용자가 본인 매장의 메뉴를 추가합니다.")
    public ResponseEntity<ApiResponseData<CreateMenuResponseDto>> createMenu(@RequestBody CreateMenuRequestDto requestDto){

        CreateMenuResponseDto menu = menuService.createMenu(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponseData.success(menu));
    }

    // 7.2 특정 매장의 메뉴 목록 조회
    @GetMapping("/menus")
    @Operation(summary = "특정 매장의 전체 메뉴 조회", description = "매장의 메뉴를 조회합니다.")
    public ResponseEntity<ApiResponseData<List<CreateMenuResponseDto>>> findAllMenus(@RequestParam("storeId") UUID storeId) {
        // 매장 ID에 해당하는 메뉴 목록을 조회
        List<CreateMenuResponseDto> menuResponseDto = menuService.findAllMenus(storeId);

        // OK 응답 반환
        return ResponseEntity.ok().body(ApiResponseData.success(menuResponseDto));
    }

    // 7.3 메뉴 상세 조회
    @GetMapping("/menus/{menuId}")
    @Operation(summary = "메뉴 상세 조회", description = "특정 메뉴를 조회합니다.")
    public ResponseEntity<ApiResponseData<CreateMenuResponseDto>> findMenus(@PathVariable UUID menuId) {
        // 매장 ID에 해당하는 메뉴 목록을 조회
        CreateMenuResponseDto findMenu = menuService.findMenus(menuId);

        // OK 응답 반환
        return ResponseEntity.ok().body(ApiResponseData.success(findMenu));
    }

    // 7.4 메뉴 수정
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @PutMapping("/menus/{menuId}")
    @Operation(summary = "메뉴 수정", description = "사용자가 본인 매장의 메뉴를 수정합니다.")
    public ResponseEntity<ApiResponseData<CreateMenuResponseDto>> updateMenu(@PathVariable UUID menuId,
                                                            @RequestBody CreateMenuRequestDto requestDto) {
        CreateMenuResponseDto menuUpdate = menuService.updateMenu(menuId, requestDto);

        return ResponseEntity.ok().body(ApiResponseData.success(menuUpdate));
    }

    // 7.5 메뉴 삭제
    @PreAuthorize("hasAnyRole('OWNER','MANAGER')")
    @DeleteMapping("/menus/{menuId}")
    @Operation(summary = "메뉴 삭제", description = "사용자가 본인 매장의 메뉴를 삭제합니다.")
    public ResponseEntity<ApiResponseData<Void>> deleteMenu(@PathVariable UUID menuId) {
        menuService.deleteMenu(menuId);

        return ResponseEntity.ok().body(ApiResponseData.success(null, "메뉴가 삭제되었습니다."));
    }
}
