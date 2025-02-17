package com.sparta.bedelivery.entity.service;

import com.sparta.bedelivery.entity.LocationCategory;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.entity.StoreIndustryCategory;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.entity.dto.StoreRequestDto;
import com.sparta.bedelivery.entity.repository.LocationCategoryRepository;
import com.sparta.bedelivery.entity.repository.StoreIndustryCategoryRepository;
import com.sparta.bedelivery.entity.repository.StoreRepository;
import com.sparta.bedelivery.entity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class StoreServiceTest {


    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreIndustryCategoryRepository storeIndustryCategoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private StoreService storeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    @DisplayName("매장 등록하는 테스트")
    void addStore_Success() {
        // Given
        UUID locationCategoryId = UUID.randomUUID();
        Long userId = 1L;

        StoreIndustryCategory storeIndustryCategory = new StoreIndustryCategory();
        storeIndustryCategory.setIndustryCategory("61f5f6ea-e37d-46ff-9849-b0f831862859");

        User user = new User();
        user.setId(userId);

        StoreRequestDto requestDto = new StoreRequestDto();
        requestDto.setName("맛있는 치킨");
        requestDto.setLocationCategoryId(UUID.fromString(locationCategoryId.toString()));
        requestDto.setAddress("서울 강남구");
        requestDto.setPhone("0212345678");
        requestDto.setImageUrl("https://example.com/image.jpg");

        Store store = new Store();
        store.setName(requestDto.getName());
        store.setAddress(requestDto.getAddress());
        store.setPhone(requestDto.getPhone());
        store.setImageUrl(requestDto.getImageUrl());

        when(locationCategoryRepository.findById(locationCategoryId)).thenReturn(Optional.of(locationCategory));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(storeRepository.save(any(Store.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Store createdStore = storeService.addStore(requestDto, userId);

        // Then
        assertNotNull(createdStore);
        assertEquals("맛있는 치킨", createdStore.getName());
        assertEquals("서울 강남구", createdStore.getAddress());
        assertEquals("0212345678", createdStore.getPhone());
        assertEquals("https://example.com/image.jpg", createdStore.getImageUrl());
        assertEquals(Store.Status.OPEN, createdStore.getStatus());
        assertEquals(locationCategory, createdStore.getLocationCategory());
        assertEquals(user, createdStore.getUser());

        verify(locationCategoryRepository).findById(locationCategoryId);
        verify(userRepository).findById(userId);
        verify(storeRepository).save(any(Store.class));
    }
}