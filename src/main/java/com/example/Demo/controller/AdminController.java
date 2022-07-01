package com.example.Demo.controller;


import com.example.Demo.dto.SuccessDto;
import com.example.Demo.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;


    @PutMapping("/user/{username}")
    public ResponseEntity<SuccessDto> setAccess(@PathVariable String username, @RequestParam boolean blocked) {
        adminService.setAccessUser(username, blocked);
        return ResponseEntity.ok(new SuccessDto("Access changed"));
    }


    @PutMapping("{username}/roles/")
    public ResponseEntity<SuccessDto> giveRole(@PathVariable String username, @RequestParam String role) {
        adminService.giveRole(username, role);
        return ResponseEntity.ok(new SuccessDto("Role changed"));
    }

}
