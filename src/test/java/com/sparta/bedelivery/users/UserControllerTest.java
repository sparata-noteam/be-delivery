package com.sparta.bedelivery.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.dto.AuthRequest;
import com.sparta.bedelivery.dto.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetUserInfo_Success() throws Exception {
        // 로그인하여 인증 토큰을 얻기
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("testuser1");
        authRequest.setPassword("newPassword123@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 로그인 응답에서 Authorization 헤더 (Bearer token) 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        // 내 정보 조회 요청
        mockMvc.perform(get("/api/users/me")
                        .header("Authorization", token))  // 인증된 토큰 추가
                .andExpect(MockMvcResultMatchers.status().isOk())  // 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value("testuser1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value("CUSTOMER"));
    }

    @Test
    public void testUpdateUser_Success() throws Exception {
        // 로그인하여 인증 토큰을 얻기
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("testuser1");
        authRequest.setPassword("newPassword123@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 로그인 응답에서 Authorization 헤더 (Bearer token) 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        // 수정할 사용자 정보 (UserUpdateRequest)
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setNickName("UpdatedNickname");
        userUpdateRequest.setPhone("01098745635");

        // 내 정보 수정 요청
        mockMvc.perform(put("/api/users/me")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdateRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())  // 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickName").value("UpdatedNickname"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value("01098745635"));
    }

    @Test
    public void testDeleteUser_Success() throws Exception {
        // 로그인하여 인증 토큰을 얻기
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("testuser1");
        authRequest.setPassword("newPassword123@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 로그인 응답에서 Authorization 헤더 (Bearer token) 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        // 사용자 탈퇴 요청
        mockMvc.perform(delete("/api/users/me")
                        .header("Authorization", token))  // 인증된 토큰 추가
                .andExpect(MockMvcResultMatchers.status().isOk())  // 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("testuser1 계정이 삭제되었습니다."));
    }
}
