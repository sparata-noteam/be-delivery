package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.dto.UserResponse;
import com.sparta.bedelivery.entity.User;
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
}
