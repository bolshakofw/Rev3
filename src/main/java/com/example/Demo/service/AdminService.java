package com.example.Demo.service;


import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.errors.exception.permission.PermissionException;
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

    //todo объединить методы раз/блокировки*
    public void accessUser(String username, boolean access) {

        //todo убрать .get(), сделать orElseThrow()*
        UserProfile userProfile = userRepository.findByUsername(username).orElseThrow();

        userProfile.setAccess(access);
        userRepository.save(userProfile);
    }


//    public void giveRole(String username, String role) {
//        UserProfile currentUser = authService.getCurrentUser();
//        UserProfile userProfile = userRepository.findByUsername(username).orElseThrow();
//        Role role1 = roleRepository.findByRole("ROLE_" + role).orElseThrow();
//
//        Set<Role> roles = userProfile.getRoles();
//        if (roles.contains(role)) {
//            throw new PermissionException("User is already admin");
//        }
//
//        roles.add(role);
//        // todo вынести обращение в базу за текущим юзером сюда
//        userProfile.setAdmin(currentUser);
//        userRepository.save(userProfile);
//
//
//    }


    public void giveRole(String username,String role){

        UserProfile userProfile = userRepository.findByUsername(username).orElseThrow();
        Role giveRole = roleRepository.findByRole("ROLE_" + role).orElseThrow();

        Set<Role> roles = userProfile.getRoles();
        if (roles.contains(role)) {
            throw new PermissionException("User is already admin");
        }
        roles.add(giveRole);
        userProfile.setAdmin(authService.getCurrentUser());
        userRepository.save(userProfile);
    }


}
