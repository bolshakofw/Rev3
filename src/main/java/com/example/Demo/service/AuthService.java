package com.example.Demo.service;


import com.example.Demo.dto.LoginDto;
import com.example.Demo.dto.SignUpDto;
import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.errors.exception.permission.PermissionException;
import com.example.Demo.repository.RoleRepository;
import com.example.Demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AuthService {


    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;


    public List<String> roles;

    public ResponseEntity<String> signin(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
    }

    public ResponseEntity<String> signup(SignUpDto signUpDto) {
        if (userRepository.existsByUsername(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        UserProfile userProfile = new UserProfile();
        userProfile.setName(signUpDto.getName());
        userProfile.setUsername(signUpDto.getUsername());
        userProfile.setEmail(signUpDto.getEmail());
        userProfile.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        userProfile.setAcces(true);

        if (!roles.contains("ADMIN") && roleRepository.findByRole("ADMIN").isPresent()) {
            Role role = roleRepository.findByRole("ADMIN").get();
            userProfile.setRoles(Collections.singleton(role));
            roles.add("ADMIN");
        } else if (roles.contains("ADMIN") && roleRepository.findByRole("USER").isPresent()) {
            Role role = roleRepository.findByRole("USER").get();
            userProfile.setRoles(Collections.singleton(role));
            roles.add("USER");
        }


        userRepository.save(userProfile);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }


    public UserProfile getUserByUsernameOrEmail() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userRepository.findByEmail(user.getUsername()).isPresent()) {
            return userRepository.findByEmail(user.getUsername()).get();
        } else {
            return null;
        }
    }


    public void accessUserCheck() {
        if (roleRepository.findByRole("USER").isPresent()) {
            Role role = roleRepository.findByRole("USER").get();

            if (!getUserByUsernameOrEmail().getRoles().contains(role) || !getUserByUsernameOrEmail().isAcces()) {
                throw  new PermissionException("Админу не доступна выбранная функция или доступ ограничен");
            }
        }
    }


    public void accessAdminCheck(){
        if (roleRepository.findByRole("ADMIN").isPresent()) {
            Role role = roleRepository.findByRole("ADMIN").get();

            if (!getUserByUsernameOrEmail().getRoles().contains(role) || !getUserByUsernameOrEmail().isAcces()) {
                throw  new PermissionException("Пользователю недоступна выбранная функция или доступ ограничен");
            }
        }
    }


}
