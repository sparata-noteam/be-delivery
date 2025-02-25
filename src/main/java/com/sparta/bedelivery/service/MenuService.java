package com.sparta.bedelivery.service;

import com.sparta.bedelivery.dto.menu.CreateMenuRequestDto;
import com.sparta.bedelivery.dto.menu.CreateMenuResponseDto;
import com.sparta.bedelivery.entity.Menu;
import com.sparta.bedelivery.entity.MenuImage;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.repository.MenuImageRepository;
import com.sparta.bedelivery.repository.MenuRepository;
import com.sparta.bedelivery.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final StoreRepository storeRepository;

    private final MenuImageRepository menuImageRepository;

    @Transactional
    public CreateMenuResponseDto createMenu(CreateMenuRequestDto requestDto) {
        if(menuRepository.findByName(requestDto.getName()).isPresent()){
            throw new IllegalArgumentException("해당 메뉴는 존재합니다.");
        }
        Store store = storeRepository.findByAddress(requestDto.getStoreAddress())
                .orElseThrow(() -> new IllegalArgumentException("매장을 찾을 수 없습니다."));

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

        List<String> imageUrls = requestDto.getImageUrl()!=null?
                requestDto.getImageUrl() : Collections.singletonList("이미지가 존재하지 않습니다.");

        int orderIndex = 0;
        for (String imageUrl : imageUrls) {
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
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴는 없습니다"));

        if (!menu.getIsHidden()) {
            return new CreateMenuResponseDto(menu);
        }
        throw new IllegalArgumentException("해당 메뉴는 없습니다.");
    }

    @Transactional
    public CreateMenuResponseDto updateMenu(UUID menuId, CreateMenuRequestDto requestDto) {
        if(menuRepository.findByName(requestDto.getName()).isPresent()){
            throw new IllegalArgumentException("해당 메뉴는 존재합니다.");
        }
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new IllegalArgumentException("해당 메뉴는 없습니다." + menuId));

        menu.setName(requestDto.getName());
        menu.setPrice(requestDto.getPrice());
        menu.setDescription(requestDto.getDescription());
        menu.setIsHidden(requestDto.getIsHidden());

        menu = menuRepository.save(menu);

        List<MenuImage> menuImages = new ArrayList<>();

        List<String> imageUrls = requestDto.getImageUrl()!=null?
                requestDto.getImageUrl() : Collections.singletonList("이미지가 존재하지 않습니다.");

        int orderIndex = 0;
        for (String imageUrl : imageUrls) {
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

    @Transactional
    public void deleteMenu(UUID menuId) {
        Menu menu = menuRepository.findById(menuId).
                orElseThrow(()->new IllegalArgumentException("메뉴를 찾을 수 없습니다."));

        menu.setIsHidden(true);
        menuRepository.save(menu);
    }
}
