package com.sparta.bedelivery.entity.service;

import com.sparta.bedelivery.entity.LocationCategory;
import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.entity.dto.StoreRequestDto;
import com.sparta.bedelivery.entity.repository.IndustryCategoryRepository;
import com.sparta.bedelivery.entity.repository.StoreRepository;
import com.sparta.bedelivery.entity.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StoreService {
    
    private final StoreRepository storeRepository;

    private final IndustryCategoryRepository industryCategoryRepository;

    private final UserRepository userRepository;


    @Transactional
    public Store addStore(StoreRequestDto requestDto, Long userId) {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        LocationCategory locationCategory = industryCategoryRepository.findById(requestDto.get())
                .orElseThrow(() -> new EntityNotFoundException("Location category not found"));

        Store store = Store.builder()
                .name(requestDto.getName())
                .address(requestDto.getAddress())
                .phone(requestDto.getPhone())
                .imageUrl(requestDto.getImageUrl())
                .locationCategory(locationCategory)
                .build();

        return storeRepository.save(store);
    }
}
