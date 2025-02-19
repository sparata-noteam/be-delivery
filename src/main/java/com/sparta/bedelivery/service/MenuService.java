package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.CreateMenuResponseDto;
import com.sparta.bedelivery.entity.Menu;
import com.sparta.bedelivery.entity.MenuImage;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.repository.MenuImageRepository;
import com.sparta.bedelivery.repository.MenuRepository;
import com.sparta.bedelivery.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final StoreRepository storeRepository;

    private final MenuImageRepository menuImageRepository;

    public CreateMenuResponseDto createMenu(CreateMenuRequestDto requestDto) {
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

        menu = menuRepository.save(menu);

        List<MenuImage> menuImages = new ArrayList<>();
        int orderIndex = 0;
        for (String imageUrl : requestDto.getImageUrl()) {
            MenuImage menuImage = new MenuImage();
            menuImage.setImageUrl(imageUrl);
            menuImage.setOrderIndex(orderIndex++);
            menuImage.setMenu(menu);
            menuImages.add(menuImage);
        }

        menuImageRepository.saveAll(menuImages);

        menu.setImageList(menuImages);
        menuRepository.save(menu);  // 변경 사항 저장

        return new CreateMenuResponseDto(menu);
    }

    public List<CreateMenuResponseDto> findAllMenus(UUID storeId) {
        List<Menu> menus = menuRepository.findByStore_Id(storeId);

        return menus.stream().filter(
                menu -> !menu.getIsHidden()
        ).map(CreateMenuResponseDto::new).toList();
    }

    public CreateMenuResponseDto findMenus(UUID menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(RuntimeException::new);

        if (!menu.getIsHidden()) {
            return new CreateMenuResponseDto(menu);
        }
        throw new RuntimeException("해당 메뉴는 없습니다.");
    }

    public CreateMenuResponseDto updateMenu(UUID menuId, CreateMenuRequestDto requestDto) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new EntityNotFoundException("Menu not found with ID: " + menuId));


        menu.setName(requestDto.getName());
        menu.setPrice(requestDto.getPrice());
        menu.setDescription(requestDto.getDescription());
        menu.setIsHidden(requestDto.getIsHidden());

        menu = menuRepository.save(menu);

        List<MenuImage> menuImages = new ArrayList<>();
        int orderIndex = 0;
        for (String imageUrl : requestDto.getImageUrl()) {
            MenuImage menuImage = new MenuImage();
            menuImage.setImageUrl(imageUrl);
            menuImage.setOrderIndex(orderIndex++);
            menuImage.setMenu(menu);
            menuImages.add(menuImage);
        }

        menuImageRepository.saveAll(menuImages);

        menu.setImageList(menuImages);
        menuRepository.save(menu);

        return new CreateMenuResponseDto(menu);
    }

    public void deleteMenu(UUID menuId) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(RuntimeException::new);

        menu.setIsHidden(true);
        menuRepository.save(menu);
    }
}
