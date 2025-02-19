package com.sparta.bedelivery.dto;


import com.sparta.bedelivery.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
// 매장 수정 요청 사항을 전달할 클래스
public class StoreUpdateRequestDto {
    private String name;
    private String address;
    private String phone;
    private String imageUrl;

    @Builder
    public Store StoreUpdate() {
        return Store.builder()
                .name(name)
                .address(address)
                .phone(phone)
                .imageUrl(imageUrl)
                .build();
    }
}
