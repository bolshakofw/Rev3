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


    // todo admin/user/username?blocked=true or false
    @PutMapping("/accessUser/{username}/")
    public ResponseEntity<SuccessDto> setAccess(@PathVariable String username, @RequestBody boolean access) {
        adminService.setAccessUser(username, access);
        return ResponseEntity.ok(new SuccessDto("Access changed"));
    }


    // todo PUT /api/admin/{username}/roles?role=ADMIN
    @PutMapping("{username}/roles/")
    public ResponseEntity<SuccessDto> giveRole(@PathVariable String username, @RequestBody String role) {
        adminService.giveRole(username, role);
        return ResponseEntity.ok(new SuccessDto("Role changed"));
    }

}
