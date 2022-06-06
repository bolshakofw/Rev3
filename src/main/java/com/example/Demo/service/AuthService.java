package com.example.Demo.service;


import com.example.Demo.dto.LoginDto;
import com.example.Demo.dto.SignUpDto;
import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
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

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class AuthService {


    public List<String> roles;
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

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

        if (!roles.contains("ROLE_ADMIN") && roleRepository.findByRole("ROLE_ADMIN").isPresent()) {
            Role role = roleRepository.findByRole("ROLE_ADMIN").get();
            userProfile.setRoles(Collections.singleton(role));
            roles.add("ROLE_ADMIN");
            userProfile.setAdmin(userProfile);
        } else if (roles.contains("ROLE_ADMIN") && roleRepository.findByRole("ROLE_USER").isPresent()) {
            Role role = roleRepository.findByRole("ROLE_USER").get();
            userProfile.setRoles(Collections.singleton(role));
            roles.add("ROLE_USER");
        }

        userRepository.save(userProfile);

        return new ResponseEntity<>("Registered successfully", HttpStatus.OK);
    }


    public UserProfile getCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userRepository.findByEmail(user.getUsername()).isPresent()) {
            return userRepository.findByEmail(user.getUsername()).get();
        } else {
            return null;
        }
    }


    public void changePass(String newpas) {
        UserProfile userProfile = getCurrentUser();
        userProfile.setPassword(passwordEncoder.encode(newpas));
        userProfile.setPasschange(new Timestamp(System.currentTimeMillis()));
        userRepository.save(userProfile);
    }


}
