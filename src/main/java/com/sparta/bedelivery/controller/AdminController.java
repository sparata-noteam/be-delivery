package com.sparta.bedelivery.controller;

import com.sparta.bedelivery.dto.RoleUpdateRequest;
import com.sparta.bedelivery.dto.UserResponse;
import com.sparta.bedelivery.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // 1.8 사용자 목록 조회
    @GetMapping("/users")
    public ResponseEntity<Page<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Pageable pageable = PageRequest.of(
                page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)
        );

        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    // 1.9 특정 사용자 상세 조회
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }

    // 1.10 특정 사용자 강제 탈퇴
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteUserByAdmin(@PathVariable Long userId) {
        adminService.deleteUserByAdmin(userId);
        return ResponseEntity.ok("{\"message\": \"사용자가 삭제되었습니다.\"}");
    }

    // 1.11 사용자 권한 변경
    @PatchMapping("/users/{userId}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @RequestBody RoleUpdateRequest request) {
        adminService.updateUserRole(userId, request);
        return ResponseEntity.ok("{\"message\": \"User role updated successfully\"}");
    }
}
