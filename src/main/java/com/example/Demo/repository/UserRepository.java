package com.example.Demo.repository;

import com.example.Demo.entity.UserProfile;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserProfile, UUID>  {
        Optional<UserProfile> findByEmail(String email);
        Optional<UserProfile> findByUsernameOrEmail(String username,String email);
        Optional<UserProfile> findByUsername(String username);
        Boolean existsByUsername(String username);
        Boolean existsByEmail(String email);
}
