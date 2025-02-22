package com.sparta.bedelivery.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int statusCode = HttpServletResponse.SC_UNAUTHORIZED; // 기본값 401 Unauthorized
        String message = exception.getMessage();

        if (message.startsWith("403:")) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            message = message.substring(4).trim();
        } else if (message.startsWith("404:")) {
            statusCode = HttpServletResponse.SC_NOT_FOUND;
            message = message.substring(4).trim();
        }

        response.setStatus(statusCode);

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", statusCode);
        errorResponse.put("message", message);

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
