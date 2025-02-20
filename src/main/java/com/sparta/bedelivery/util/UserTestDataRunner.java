package com.sparta.bedelivery.util;

import com.sparta.bedelivery.dto.UserRegisterRequest;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.entity.UserAddress;
import com.sparta.bedelivery.repository.UserAddressRepository;
import com.sparta.bedelivery.repository.UserRepository;
import com.sparta.bedelivery.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class UserTestDataRunner {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    private static final String[] phonePrefixes = {"010"};
    private static final Random random = new Random();
    private static final Set<String> usedPhones = new HashSet<>();

    private static final String[] gwanghwamunAddresses = {
            "서울 종로구 세종대로",
            "서울 종로구 새문안로",
            "서울 종로구 사직로",
            "서울 종로구 자하문로",
            "서울 종로구 종로1길",
            "서울 종로구 경희궁길"
    };

    @Bean
    public ApplicationRunner dataLoader() {
        return args -> {
            List<UserRegisterRequest> testUsers = IntStream.rangeClosed(1, 30)
                    .mapToObj(i -> new UserRegisterRequest(
                            "customer" + i,
                            "Password1!",
                            "고객" + i,
                            "nick_c" + i,
                            generateUniquePhoneNumber(),
                            "CUSTOMER"))
                    .collect(Collectors.toList());

            testUsers.addAll(IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> new UserRegisterRequest(
                            "owner" + i,
                            "Password1!",
                            "점주" + i,
                            "nick_o" + i,
                            generateUniquePhoneNumber(),
                            "OWNER"))
                    .collect(Collectors.toList()));

            testUsers.addAll(IntStream.rangeClosed(1, 10)
                    .mapToObj(i -> new UserRegisterRequest(
                            "manager" + i,
                            "Password1!",
                            "매니저" + i,
                            "nick_m" + i,
                            generateUniquePhoneNumber(),
                            "MANAGER"))
                    .collect(Collectors.toList()));

            testUsers.add(new UserRegisterRequest(
                    "master01", "Master1@", "최고관리자1", "nick_master1", generateUniquePhoneNumber(), "MASTER"));
            testUsers.add(new UserRegisterRequest(
                    "master02", "Master2#", "최고관리자2", "nick_master2", generateUniquePhoneNumber(), "MASTER"));

            for (UserRegisterRequest request : testUsers) {
                if (!userRepository.existsByUserIdAndDeleteAtIsNull(request.getUserId())) {
                    User user = authService.registerUser(request);
                    System.out.println("Test user created: " + request.getUserId());

                    // CUSTOMER인 경우 배송지 등록
                    if ("CUSTOMER".equals(request.getRole())) {
                        addUserAddresses(user);
                    }
                } else {
                    System.out.println("Test user already exists: " + request.getUserId());
                }
            }
        };
    }

    /**
     * 중복되지 않는 유니크한 전화번호 생성
     */
    private String generateUniquePhoneNumber() {
        String phone;
        do {
            String prefix = phonePrefixes[random.nextInt(phonePrefixes.length)];
            String number = String.format("%07d", random.nextInt(10000000));
            phone = prefix + number;
        } while (usedPhones.contains(phone) || userRepository.existsByPhone(phone));

        usedPhones.add(phone);
        return phone;
    }

    /**
     * CUSTOMER에게 3개의 배송지 추가 (첫 번째 배송지는 기본 배송지)
     */
    @Transactional
    public void addUserAddresses(User user) {
        List<UserAddress> addresses = IntStream.rangeClosed(1, 3)
                .mapToObj(i -> {
                    String baseAddress = gwanghwamunAddresses[random.nextInt(gwanghwamunAddresses.length)];
                    return new UserAddress(
                            user,
                            "배송지" + i,  // 주소 이름
                            user.getName(),  // 수령인 이름
                            baseAddress + " " + (random.nextInt(100) + 1) + "번지",  // 광화문 지역 랜덤 주소
                            "03" + (random.nextInt(100) + 100),  // 우편번호 (03000~03100)
                            i == 1 // 첫 번째 주소를 기본 배송지로 설정
                    );
                })
                .collect(Collectors.toList());

        userAddressRepository.saveAll(addresses);
        System.out.println("배송지 등록 완료: " + user.getUserId());
    }
}
