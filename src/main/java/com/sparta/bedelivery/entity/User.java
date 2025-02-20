package com.sparta.bedelivery.entity;

import com.sparta.bedelivery.dto.ChangePasswordRequest;
import com.sparta.bedelivery.dto.UserRegisterRequest;
import com.sparta.bedelivery.dto.UserUpdateRequest;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "p_users")
public class User extends BaseSystemFieldEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String userId; //로그인 id

    @Column(nullable = false, unique = true, length = 255)
    private String nickname;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role = Role.CUSTOMER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses;

    public User(UserRegisterRequest request, PasswordEncoder passwordEncoder) {
        this.userId = request.getUserId();
        this.nickname = request.getNickName();
        this.password = passwordEncoder.encode(request.getPassword());
        this.name = request.getName();
        this.phone = request.getPhone();
        this.role = Role.fromString(request.getRole());
        this.setCreateBy(request.getUserId());
    }

    public User(String userId) {
        this.userId = userId;
    }

//    public User(String username, String email, String encode, String name, String phone, String role) {
//        super();
//        this.username = username;
//        this.email = email;
//        this.password = encode;
//        this.name = name;
//        this.phone = phone;
//        this.role = Role.valueOf(role);
//    }

    public void updatePassword(ChangePasswordRequest request, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(request.getNewPassword());
    }


    public void updateInfo(UserUpdateRequest request) {
        this.nickname = request.getNickName();
        this.phone = request.getPhone();
    }


    public enum Role {
        CUSTOMER, OWNER, MANAGER, MASTER;

        public static Role fromString(String roleStr) {
            try {
                return Role.valueOf(roleStr.toUpperCase());
            } catch (IllegalArgumentException | NullPointerException e) {
                return Role.CUSTOMER; // 기본값 설정
            }
        }

        public static Role findRole(String authority) {
            return Arrays.stream(values()).filter(r -> authority.contains(r.name())).findFirst().orElseThrow(
                    () -> new IllegalArgumentException("해당하는 권한은 존재하지 않습니다.")
            );
        }
    }

    @OneToMany
    @JoinColumn(name = "user_address_id") // 한 명의 사용자는 여러 개의 주소를 관리한다.
    private List<UserAddress> userAddressList = new ArrayList<>();

//    @OneToMany(mappedBy = "user")
//    private List<Order> orderList = new ArrayList<>();

    @OneToMany
//    @JoinColumn(name = "store_id")
    private List<Store> storeList = new ArrayList<>();
}
