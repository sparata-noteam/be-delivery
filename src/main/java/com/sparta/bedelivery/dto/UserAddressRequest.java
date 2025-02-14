package com.sparta.bedelivery.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserAddressRequest {

    @NotBlank(message = "주소 이름은 필수입니다.")
    private String addressName;

    @NotBlank(message = "수령인 이름은 필수입니다.")
    private String recipientName;

    @NotBlank(message = "상세 주소는 필수입니다.")
    private String address;

    @NotBlank(message = "우편번호는 필수입니다.")
    private String zipCode;

    @NotNull(message = "기본 배송지 여부를 설정해야 합니다.")
    private Boolean isDefault;


}
