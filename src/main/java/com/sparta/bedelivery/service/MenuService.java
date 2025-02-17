package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.CreateMenuResponseDto;
import com.sparta.bedelivery.entity.Menu;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.repository.MenuRepository;
import com.sparta.bedelivery.repository.StoreRepository;
import com.sparta.bedelivery.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final StoreRepository storeRepository;

    private final JwtUtil jwtUtil;

    public Menu createMenu(CreateMenuRequestDto requestDto, HttpServletRequest request) {
        // 토큰 검증
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("유효한 인증 토큰이 필요합니다.");
        }
        String tokenValue = token.substring(7);
        String role = jwtUtil.extractRole(tokenValue);
        // 권한 체크
        if (!("OWNER".equals(role) || "MANAGER".equals(role))) {
            throw new RuntimeException("매장 운영 권한이 필요합니다.");
        }
        Store store = storeRepository.findById(requestDto.getStoreId())
                .orElseThrow(() -> new RuntimeException("매장을 찾을 수 없습니다."));

        // 메뉴 생성
        Menu menu = Menu.builder()
                .name(requestDto.getName())
                .price(requestDto.getPrice())
                .description(requestDto.getDescription())
                .isHidden(requestDto.getIsHidden())
                .store(store)  // Store 객체 설정
                .build();

        Menu savedMenu = menuRepository.save(menu);

        return savedMenu;
    }

    public List<CreateMenuResponseDto> findAllMenus(UUID storeId) {
        List<Menu> menus = menuRepository.findByStore_Id(storeId);

        // 메뉴 엔티티를 CreateMenuResponseDto로 변환
        return menus.stream().map(CreateMenuResponseDto::new)
                .toList();
    }

    public CreateMenuResponseDto findMenus(UUID menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(RuntimeException::new);
        if (!menu.getIsHidden()) {
            return new CreateMenuResponseDto(menu);
        }
        throw new RuntimeException("해당 메뉴는 없습니다.");
    }

    public Menu updateMenu(UUID menuId, CreateMenuRequestDto requestDto, HttpServletRequest request) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(RuntimeException::new);

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("유효한 인증 토큰이 필요합니다.");
        }
        String tokenValue = token.substring(7);
        String role = jwtUtil.extractRole(tokenValue);
        // 권한 체크
        if (!("OWNER".equals(role) || "MANAGER".equals(role))) {
            throw new RuntimeException("매장 운영 권한이 필요합니다.");
        }
        menu.setName(requestDto.getName());
        menu.setPrice(requestDto.getPrice());
        menu.setDescription(requestDto.getDescription());
        menu.setIsHidden(requestDto.getIsHidden());

        menuRepository.save(menu);

        return menu;
    }

    public CreateMenuResponseDto deleteMenu(UUID menuId, HttpServletRequest request) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(RuntimeException::new);

        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new RuntimeException("유효한 인증 토큰이 필요합니다.");
        }
        String tokenValue = token.substring(7);
        String role = jwtUtil.extractRole(tokenValue);
        // 권한 체크
        if (!("OWNER".equals(role) || "MANAGER".equals(role))) {
            throw new RuntimeException("매장 운영 권한이 필요합니다.");
        }
        menu.setIsHidden(true);
        menuRepository.save(menu);

        return new CreateMenuResponseDto(menu);
    }
}
