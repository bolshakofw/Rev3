package com.example.Demo.service;


import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.errors.exception.users.AdminException;
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

    //todo объединить методы раз/блокировки
    public void blockUser(String username, boolean acces) {

        //todo убрать .get(), сделать orElseThrow()
        UserProfile userProfile = userRepository.findByUsername(username).orElseThrow();

        userProfile.setAcces(acces);
        userRepository.save(userProfile);
    }

    public void unblockUser(String username) {

        UserProfile userProfile = userRepository.findByUsername(username).get();

        userProfile.setAcces/*todo s*/(true);
        userRepository.save(userProfile);
    }


    public void op(String username) {
        UserProfile currentUser = authService.getCurrentUser();
        UserProfile userProfile = userRepository.findByUsername(username).get();
        Role role = roleRepository.findByRole("ROLE_ADMIN").get();

        Set<Role> roles = userProfile.getRoles();
        if (roles.contains(role)) {
            throw new AdminException("User is already admin");
        }

        roles.add(role);
        //todo вынести обращение в базу за текущим юзером сюда
        userProfile.setAdmin(currentUser);
        userRepository.save(userProfile);


    }

    public void deOp(String username) {
        UserProfile currentUser = authService.getCurrentUser();
        UserProfile userProfile = userRepository.findByUsername(username).get();
        if (currentUser.getAdmin().equals(userProfile)) {
            throw new AdminException("No permission to do this");
        }

        Role role = roleRepository.findByRole("ROLE_USER").get();
        Set<Role> roles = userProfile.getRoles();
        roles.clear();
        roles.add(role);
        userRepository.save(userProfile);


    }

}
