package com.sparta.bedelivery.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.dto.user.AuthRequest;
import com.sparta.bedelivery.dto.user.RoleUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // 사용자 목록 조회 테스트
    @Test
    public void testGetAllUsers_Success() throws Exception {
        // 로그인 요청
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("admin1");
        authRequest.setPassword("adminPassword1@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 로그인 후 Bearer Token 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        // 사용자 목록 조회 요청
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", token))  // 인증된 토큰 추가
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllUsers_fail() throws Exception {
        // 로그인 요청
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("testuser2"); //일반 사용자
        authRequest.setPassword("Password123@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 로그인 후 Bearer Token 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        // 사용자 목록 조회 요청 (일반 사용자가 접근할 때 403 에러 발생)
        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", token))  // 인증된 토큰 추가
                .andExpect(status().isForbidden());  // 403 Forbidden 응답 기대
    }

    // 특정 사용자 상세 조회 테스트
    @Test
    public void testGetUserById_Success() throws Exception {
        // 로그인 요청
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("admin1");
        authRequest.setPassword("adminPassword1@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 로그인 후 Bearer Token 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        long userId = 1L;
        mockMvc.perform(get("/api/admin/users/{userId}", userId)
                        .header("Authorization", token))  // 인증된 토큰 추가
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").exists());
    }

    // 특정 사용자 강제 탈퇴 테스트
    @Test
    public void testDeleteUserByAdmin_Success() throws Exception {
        // 로그인 요청
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("admin1");
        authRequest.setPassword("adminPassword1@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 로그인 후 Bearer Token 추출
        String token = loginResult.getResponse().getHeader("Authorization");
        long userId = 1L;
        mockMvc.perform(delete("/api/admin/users/{userId}", userId)
                        .header("Authorization", token))  // 인증된 토큰 추가
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("사용자가 삭제되었습니다."));
    }

    // 사용자 권한 변경 테스트
    @Test
    public void testUpdateUserRole_Success() throws Exception {
        // 로그인 요청
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("admin1");
        authRequest.setPassword("adminPassword1@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 로그인 후 Bearer Token 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        long userId = 2L;
        RoleUpdateRequest request = new RoleUpdateRequest();
        request.setRole("OWNER");

        mockMvc.perform(patch("/api/admin/users/{userId}/role", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))  // 인증된 토큰 추가
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User role updated successfully"));
    }

    // 예시로 비정상적인 요청 (존재하지 않는 사용자에 대한 권한 변경) 테스트
    @Test
    public void testUpdateUserRole_Failure() throws Exception {
        // 로그인 요청
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("admin1");
        authRequest.setPassword("adminPassword1@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andReturn();

        // 로그인 후 Bearer Token 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        long userId = 999L; // 존재하지 않는 사용자 ID
        RoleUpdateRequest request = new RoleUpdateRequest();
        request.setRole("MANAGER");

        mockMvc.perform(patch("/api/admin/users/{userId}/role", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", token))  // 인증된 토큰 추가
                .andExpect(status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }
}
