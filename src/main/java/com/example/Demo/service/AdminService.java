package com.example.Demo.service;


import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.repository.RoleRepository;
import com.example.Demo.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class AdminService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private AuthService authService;

//
//    public void makeAdmin(String username){
//        if(userRepository.findByUsername(username).isPresent() && roleRepository.findByRole("ADMIN").isPresent() && roleRepository.findByRole("USER").isPresent()){
//            UserProfile user = userRepository.findByUsername(username).get();
//            Role role = roleRepository.findByRole("ADMIN").get();
//            Role role1 = roleRepository.findByRole("USER").get();
//            user.deleteRole(role1);
//            user.addRole(role);
//        }
//    }


    public void blockUser(String username){
        authService.accessAdminCheck();
        UserProfile userProfile = userRepository.findByUsername(username).get();

        userProfile.setAcces(false);
        userRepository.save(userProfile);
    }

    public void unblockUser(String username){
        authService.accessAdminCheck();
        UserProfile userProfile = userRepository.findByUsername(username).get();

        userProfile.setAcces(true);
        userRepository.save(userProfile);
    }


    public void makeGod(String username){
        authService.accessAdminCheck();
        UserProfile userProfile = userRepository.findByUsername(username).get();
        Role role = roleRepository.findByRole("ADMIN").get();
        Set<Role> roles = userProfile.getRoles();
        //roles.clear();
        roles.add(role);
        userRepository.save(userProfile);

    }

    public void deOp(String username){
        authService.accessAdminCheck();
        UserProfile userProfile = userRepository.findByUsername(username).get();
        Role role = roleRepository.findByRole("USER").get();
        Set<Role> roles = userProfile.getRoles();
        roles.clear();
        roles.add(role);
        userRepository.save(userProfile);
    }

}
