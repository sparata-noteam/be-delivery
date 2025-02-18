package com.sparta.bedelivery.global.exception;

import com.sparta.bedelivery.global.response.ApiResponseData;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class CustomErrorController implements ErrorController {

    @GetMapping
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponseData<String> handleNotFoundError() {
        return ApiResponseData.failure(404, "요청한 API를 찾을 수 없습니다.");
    }
}
