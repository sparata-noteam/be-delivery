package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    void deleteById(UUID id);
    Optional<UserAddress> findById(UUID addressId);
    List<UserAddress> findByUser_UserId(String userId);

    List<UserAddress> findByUser_UserIdAndDeleteAtIsNull(String userUserId);

    Optional<UserAddress> findByIdAndDeleteAtIsNull(UUID addressId);
}
