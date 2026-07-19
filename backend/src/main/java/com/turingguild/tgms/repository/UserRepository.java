package com.turingguild.tgms.repository;

import com.turingguild.tgms.entity.User;
import com.turingguild.tgms.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByBatchNumber(String batchNumber);
    Optional<User> findByEmail(String email);
    Optional<User> findByBatchNumberOrEmail(String batchNumber, String email);
    boolean existsByBatchNumber(String batchNumber);
    boolean existsByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByRoleAndIsActive(Role role, Boolean isActive);
    long countByRole(Role role);
}
