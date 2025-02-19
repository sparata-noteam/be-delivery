package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    void deleteById(UUID id);
    Optional<UserAddress> findById(UUID addressId);
    List<UserAddress> findByUser_UserId(String userId);
}
