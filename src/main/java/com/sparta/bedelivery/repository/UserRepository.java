package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.dto.UserResponse;
import com.sparta.bedelivery.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickname(String nickname);

    boolean existsByPhone(@Pattern(
            regexp = "^010\\d{8}$",
            message = "전화번호는 010으로 시작하는 11자리 숫자만 입력 가능합니다. 예) 01012345678"
    ) String phone);

    Optional<User> findByUserIdAndDeleteAtIsNull(String userId);

    boolean existsByUserIdAndDeleteAtIsNull
            (@NotBlank(message = "아이디는 필수 입력 값입니다.")
            @Pattern(regexp = "^[a-z0-9]{4,10}$",
                    message = "아이디는 4~10자의 영문 소문자와 숫자로만 구성되어야 합니다.") String userId);

    boolean existsByNicknameAndDeleteAtIsNull(@NotBlank(message = "닉네임은 필수 입력 값입니다.") String nickName);
}
