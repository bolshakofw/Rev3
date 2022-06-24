package com.example.Demo.repository;

import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, UUID> {
    Optional<UserProfile> findByEmail(String email);

    Optional<UserProfile> findByUsernameOrEmail(String username, String email);

    Optional<UserProfile> findByUsername(String username);

    Set<UserProfile> findByRoles(Role role);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
