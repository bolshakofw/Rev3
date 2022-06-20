package com.example.Demo.service;


import com.example.Demo.dto.LoginDto;
import com.example.Demo.dto.SignUpDto;
import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.errors.exception.EmailTakenException;
import com.example.Demo.errors.exception.UsernameTakenException;
import com.example.Demo.errors.exception.users.ChangePasswordException;
import com.example.Demo.repository.RoleRepository;
import com.example.Demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Collections;

@Service
@AllArgsConstructor
public class AuthService {

    // todo убрать состояние, сделать определение админа через бд*
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;


    public void signin(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void signup(SignUpDto signUpDto) {
        // todo через ошибки *
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

        Role adminRole = roleRepository.findByRole("ROLE_ADMIN").orElseThrow();

        if (userRepository.findByRoles(adminRole).isEmpty()) {
            userProfile.setRoles(Collections.singleton(adminRole));
            userProfile.setAdmin(userProfile);
        } else {
            Role role = roleRepository.findByRole("ROLE_USER").orElseThrow();
            userProfile.setRoles(Collections.singleton(role));
        }

        userRepository.save(userProfile);

        // todo дто для успешных ответов*

    }


    public UserProfile getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(username).orElseThrow();
    }



    public void changePass(String newPassword) {
        UserProfile userProfile = getCurrentUser();

        // todo pe.matches()*

        String newEncodedPass = passwordEncoder.encode(newPassword);

        if (passwordEncoder.matches(newPassword, userProfile.getPassword())) {
            throw new ChangePasswordException("Passwords are the same");
        }

        userProfile.setPassword(newEncodedPass);
        userProfile.setPasswordChangeTime(new Timestamp(System.currentTimeMillis()));
        userRepository.save(userProfile);

    }

}
