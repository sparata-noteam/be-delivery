package com.sparta.bedelivery.entity.controller;

import com.sparta.bedelivery.entity.Store;
import com.sparta.bedelivery.entity.dto.StoreRequestDto;
import com.sparta.bedelivery.entity.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StoreController {

    private final StoreService storeService;

    @PostMapping("/stores")
    //  @AuthenticationPrincipal UserDetails userDetails, , userDetails.getUsername()
    public ResponseEntity<Store> addStore(@RequestBody StoreRequestDto requestDto, Long userId) {
        Store store = storeService.addStore(requestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(store);
    }

}
