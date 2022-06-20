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

    //todo объединить методы раз/блокировки *
    public void accessUser(String username, boolean access) {

        //todo убрать .get(), сделать orElseThrow()*
        UserProfile userProfile = userRepository.findByUsername(username).orElseThrow();
        userProfile.setAccess(true);
        userProfile.setAccess(access);
        userRepository.save(userProfile);

    }

    public void giveRole(String username, String role) {

        UserProfile userProfile = userRepository.findByUsername(username).orElseThrow();
        Role givenRole = roleRepository.findByRole("ROLE_" + role).orElseThrow();
        Role roleUser = roleRepository.findByRole("ROLE_USER").orElseThrow();
        Role roleAdmin = roleRepository.findByRole("ROLE_ADMIN").orElseThrow();
        Set<Role> roles = userProfile.getRoles();
        if(authService.getCurrentUser().getAdmin().equals(userProfile)){
            throw new PermissionException("ADMIN < ADMIN");
        }
        if (roles.contains(roleAdmin) && givenRole.equals(roleAdmin)) {
            throw new PermissionException("User is already ADMIN");
        } else if (givenRole.equals(roleUser)) {
            roles.remove(roleAdmin);
            roles.add(roleUser);
        } else {
            roles.add(givenRole);
            userProfile.setAdmin(authService.getCurrentUser());
        }
        userRepository.save(userProfile);
    }

}
