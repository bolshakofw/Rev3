package com.example.Demo.controller;


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


    @PostMapping("/signup")
    public ResponseEntity<SuccessDto> registerUser(@RequestBody SignUpDto signUpDto) {
        // todo вынести респонсентити в контроллер*
        authService.signup(signUpDto);
        return ResponseEntity.ok(new SuccessDto("Registration succeed"));
    }

    @PutMapping("/changePassword/")
    public ResponseEntity<SuccessDto> changePassword(@RequestParam String password) {
        authService.changePass(password);
        return ResponseEntity.ok(new SuccessDto("Password changed successfully"));
    }
}