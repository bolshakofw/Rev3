package com.example.Demo.controller;


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


    @PutMapping("/block/{username}")
    public void block(@PathVariable String username) {
        adminService.blockUser(username);
    }

    @PutMapping("/unblock/{username}")
    public void unblock(@PathVariable String username) {
        adminService.unblockUser(username);
    }

    @PutMapping("/op/{username}")
    public void giveAdmin(@PathVariable String username) {
        adminService.op(username);
    }

    @PutMapping("/deOp/{username}")
    public void deOp(@PathVariable String username) {
        adminService.deOp(username);
    }
}
