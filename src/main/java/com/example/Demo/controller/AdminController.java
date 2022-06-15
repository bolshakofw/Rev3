package com.example.Demo.controller;


import com.example.Demo.dto.SuccessDto;
import com.example.Demo.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;


    @PutMapping("/accessUser/{username}/{access}")
    public ResponseEntity<SuccessDto> setAccess(@PathVariable String username, @PathVariable boolean access) {
        adminService.accessUser(username, access);
        return ResponseEntity.ok(new SuccessDto("Access changed"));
    }


    // todo PUT /api/admin/{username}/roles?role=ADMIN *
    @PutMapping("{username}/roles/{role}")
    public ResponseEntity<SuccessDto> giveRole(@PathVariable String username, @PathVariable String role) {
        adminService.giveRole(username, role);
        return ResponseEntity.ok(new SuccessDto("Role changed"));
    }

}
