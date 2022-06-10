package com.example.Demo.controller;


import com.example.Demo.entity.Role;
import com.example.Demo.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@AllArgsConstructor
public class AdminController {

    private AdminService adminService;


    @PutMapping("/accesUser/{username}/{access}")
    public void setAccess(@PathVariable String username, boolean access) {
        adminService.accessUser(username, access);
    }


    // todo PUT /api/admin/{username}/roles?role=ADMIN
    @PutMapping("{username}/roles/{role}")
    public void giveRole(@PathVariable String username, String role) {
        adminService.giveRole(username, role);
    }

}
