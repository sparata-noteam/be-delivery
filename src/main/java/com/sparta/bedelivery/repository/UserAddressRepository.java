package com.sparta.bedelivery.repository;

import com.sparta.bedelivery.entity.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAddressRepository extends JpaRepository<UserAddress, UUID> {
    void deleteById(UUID id);
    Optional<UserAddress> findById(UUID addressId);
    List<UserAddress> findByUserEmail(String email);
}
