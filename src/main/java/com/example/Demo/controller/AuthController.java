package com.example.Demo.controller;


import com.example.Demo.dto.LoginDto;
import com.example.Demo.dto.SignUpDto;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        return authService.signin(loginDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        return authService.signup(signUpDto);
    }

    @PutMapping("/changepass/{pass}")
    public void changepass(String pass){
        authService.changePass(pass);
    }


}
