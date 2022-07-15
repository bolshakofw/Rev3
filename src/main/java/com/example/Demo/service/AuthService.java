package com.example.Demo.service;


import com.example.Demo.dto.SignUpDto;
import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.errors.exception.EmailTakenException;
import com.example.Demo.errors.exception.UsernameTakenException;
import com.example.Demo.errors.exception.users.ChangePasswordException;
import com.example.Demo.errors.exception.users.UserNotFoundException;
import com.example.Demo.repository.RoleRepository;
import com.example.Demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthService {


    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    public void signup(SignUpDto signUpDto) {

        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            throw new UsernameTakenException(signUpDto.getUsername() + " is already taken");
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new EmailTakenException("Email " + signUpDto.getEmail() + " is already taken");
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setName(signUpDto.getName());
        userProfile.setUsername(signUpDto.getUsername());
        userProfile.setEmail(signUpDto.getEmail());
        userProfile.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        userProfile.setAccess(true);

        Role adminRole = roleRepository.findByRole(Role.Static.ROLE_ADMIN).orElseThrow();

        if (userRepository.findByRoles(adminRole).isEmpty()) {
            userProfile.setRoles(Collections.singleton(adminRole));
            userProfile.setAdmin(userProfile);
        } else {
            Role role = roleRepository.findByRole(Role.Static.ROLE_USER).orElseThrow();
            userProfile.setRoles(Collections.singleton(role));
        }

        userRepository.save(userProfile);

    }

    public UserProfile getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfile userProfile = userRepository.findByEmail(email).orElseThrow();
        System.out.println(userProfile.getFiles());
        return userProfile;

    }

    public UserProfile getUserOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found"));
    }

    public void changePass(String newPassword) {
        UserProfile userProfile = getCurrentUser();

        String newEncodedPass = passwordEncoder.encode(newPassword);

        if (passwordEncoder.matches(newPassword, userProfile.getPassword())) {
            throw new ChangePasswordException("Passwords are the same");
        }

        userProfile.setPassword(newEncodedPass);
        userProfile.setPasswordChangeTime(new Timestamp(System.currentTimeMillis()));
        userRepository.save(userProfile);

    }


}
