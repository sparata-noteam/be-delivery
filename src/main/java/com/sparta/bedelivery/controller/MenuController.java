package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.CreateMenuResponseDto;
import com.sparta.bedelivery.entity.Menu;
import com.sparta.bedelivery.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    // 7.1 메뉴 등록
    @PostMapping("/menus")
    public ResponseEntity<CreateMenuResponseDto> createMenu(@RequestBody CreateMenuRequestDto requestDto,
                                                            HttpServletRequest request){

        Menu menu = menuService.createMenu(requestDto, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CreateMenuResponseDto(menu));
    }

    // 7.2 특정 매장의 메뉴 목록 조회
    @GetMapping("/menus")
    public ResponseEntity<List<CreateMenuResponseDto>> findAllMenus(@RequestParam("storeId") UUID storeId) {
        // 매장 ID에 해당하는 메뉴 목록을 조회
        List<CreateMenuResponseDto> menuResponseDto = menuService.findAllMenus(storeId);

        // OK 응답 반환
        return ResponseEntity.ok(menuResponseDto);
    }

    // 7.3 메뉴 상세 조회
    @GetMapping("/menus/{menuId}")
    public ResponseEntity<CreateMenuResponseDto> findMenus(@PathVariable UUID menuId) {
        // 매장 ID에 해당하는 메뉴 목록을 조회
        CreateMenuResponseDto findMenu = menuService.findMenus(menuId);

        // OK 응답 반환
        return ResponseEntity.ok(findMenu);
    }

    // 7.4 메뉴 수정
    @PutMapping("/menus/{menuId}")
    public ResponseEntity<CreateMenuResponseDto> updateMenu(@PathVariable UUID menuId,
                                                            @RequestBody CreateMenuRequestDto requestDto,
                                                            HttpServletRequest request) {
        Menu menuUpdate = menuService.updateMenu(menuId, requestDto, request);

        return ResponseEntity.ok()
                .body(new CreateMenuResponseDto(menuUpdate));
    }

    // 7.5 메뉴 삭제
    @DeleteMapping("/menus/{menuId}")
    public ResponseEntity<CreateMenuResponseDto> deleteMenu(@PathVariable UUID menuId,
                                                            HttpServletRequest request) {
        CreateMenuResponseDto menuDelete = menuService.deleteMenu(menuId, request);
        menuDelete.setMessage("메뉴가 삭제되었습니다.");

        return ResponseEntity.ok()
                .body(menuDelete);
    }
}
