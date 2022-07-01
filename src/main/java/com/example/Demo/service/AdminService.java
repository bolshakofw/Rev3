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


    public void setAccessUser(String username, boolean blocked) {
        //todo добавить текст кого искал*
        UserProfile userProfile = authService.getUserOrThrow(username);
        userProfile.setAccess(!blocked);
        userRepository.save(userProfile);
    }

    public void giveRole(String username, String role) {

        UserProfile userProfile = authService.getUserOrThrow(username);
        Role givenRole = roleRepository.findByRole(Role.Static.ROLE_PREFIX + role).orElseThrow();
        //todo вынести подобные строки в константы*
        Role roleUser = roleRepository.findByRole(Role.Static.ROLE_USER).orElseThrow();
        Role roleAdmin = roleRepository.findByRole(Role.Static.ROLE_ADMIN).orElseThrow();
        Set<Role> roles = userProfile.getRoles();
        if (authService.getCurrentUser().getAdmin().equals(userProfile)) {
            throw new PermissionException("Doesn't have permissions");
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
