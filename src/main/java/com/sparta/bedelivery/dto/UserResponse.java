package com.sparta.bedelivery.dto;

import com.sparta.bedelivery.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
        this.createdAt=user.getCreateAt();
        this.updatedAt=user.getUpdateAt();
    }
}
