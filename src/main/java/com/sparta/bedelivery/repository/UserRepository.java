package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.dto.UserResponse;
import com.sparta.bedelivery.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>  {
    Optional<User> findByUserId(String userId);
    Optional<User> findByNickname(String nickname);
}
