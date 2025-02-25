package com.sparta.bedelivery.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.bedelivery.dto.AuthRequest;
import com.sparta.bedelivery.dto.ChangePasswordRequest;
import com.sparta.bedelivery.dto.UserRegisterRequest;
import com.sparta.bedelivery.entity.User;
import com.sparta.bedelivery.global.response.ApiResponseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRegisterRequest userRegisterRequest;

    @BeforeEach
    public void setUp() {
        userRegisterRequest = new UserRegisterRequest();
        userRegisterRequest.setUserId("testuser2");
        userRegisterRequest.setPassword("Password123@");
        userRegisterRequest.setName("TestName2");
        userRegisterRequest.setNickName("testnick2");
        userRegisterRequest.setPhone("01012345672");

//        userRegisterRequest = new UserRegisterRequest();
//        userRegisterRequest.setUserId("admin1");
//        userRegisterRequest.setPassword("adminPassword1@");
//        userRegisterRequest.setName("admin1");
//        userRegisterRequest.setNickName("admin1");
//        userRegisterRequest.setRole("MASTER");
//        userRegisterRequest.setPhone("01012345888");
    }

    @Test
    public void testRegister_Success() throws Exception {
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegisterRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userId").value("testuser2"));
    }

    @Test
    public void testLogin_Success() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("testuser1");
        authRequest.setPassword("Password123@");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").exists());

    }

    @Test
    public void testLogin_Failure_InvalidCredentials() throws Exception {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("wronguser");
        authRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("로그인 실패: 404: 해당 계정이 존재하지 않습니다."));
    }

    @Test
    public void testLogout_Success() throws Exception {
        // 먼저 로그인하여 인증 토큰을 얻기
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("testuser1");
        authRequest.setPassword("newPassword123@");

        MvcResult result = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 로그인 응답에서 토큰 추출
        String token = result.getResponse().getHeader("Authorization");

        // 로그아웃 요청
        mockMvc.perform(post("/api/users/logout")
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").value("로그아웃 성공")); // data.success 대신 data 전체를 비교
    }


    @Test
    public void testChangePassword_Success() throws Exception {
        // 로그인하여 인증 토큰을 얻기
        AuthRequest authRequest = new AuthRequest();
        authRequest.setUserId("testuser1");
        authRequest.setPassword("Password123@");

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        // 로그인 응답에서 Authorization 헤더 (Bearer token) 추출
        String token = loginResult.getResponse().getHeader("Authorization");

        // 비밀번호 변경 요청
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setCurrentPassword("Password123@");
        changePasswordRequest.setNewPassword("newPassword123@");

        mockMvc.perform(post("/api/users/me/password")
                        .header("Authorization", token)  // 인증된 토큰 추가
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())  // 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.message").value("비밀번호가 성공적으로 변경되었습니다."));
    }


}
