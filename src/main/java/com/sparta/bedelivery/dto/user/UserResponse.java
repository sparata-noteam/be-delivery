package com.sparta.bedelivery.dto.user;

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
    private String userId;
    private String name;
    private String nickName;
    private String phone;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public UserResponse(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.nickName = user.getNickname();
        this.phone = user.getPhone();
        this.role = user.getRole().name();
        this.createdAt=user.getCreateAt();
        this.updatedAt=user.getUpdateAt();
    }
}
