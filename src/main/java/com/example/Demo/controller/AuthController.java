package com.example.Demo.controller;


import com.example.Demo.dto.LoginDto;
import com.example.Demo.dto.SignUpDto;
import com.example.Demo.dto.SuccessDto;
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
    public ResponseEntity<SuccessDto> registerUser(@RequestBody SignUpDto signUpDto) {
        //todo вынести респонсентити в контроллер*
        return authService.signup(signUpDto);
    }

    @PutMapping("/changePassword/")
    public ResponseEntity<SuccessDto> changePassword(@RequestParam String password) {
        return authService.changePass(password);
    }

}