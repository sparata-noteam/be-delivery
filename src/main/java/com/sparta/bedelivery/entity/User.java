package com.sparta.bedelivery.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private String username;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role= Role.CUSTOMER;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> addresses;

    public User(String username, String email, String encode, String name, String phone, String role) {
        super();
        this.username = username;
        this.email = email;
        this.password = encode;
        this.name = name;
        this.phone = phone;
        this.role = Role.valueOf(role);
    }

    public void updatePassword(String encode) {
        this.password = encode;
    }


    public void updateInfo(String name, String phone) {
        this.name = name;
        this.phone = phone;
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
    }
}
