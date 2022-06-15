package com.example.Demo.service;

import com.example.Demo.entity.Role;
import com.example.Demo.entity.UserProfile;
import com.example.Demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//todo в сервисы*
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    //todo lombok*


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfile userProfile = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email:" + username));
        return new User(userProfile.getEmail(),
                userProfile.getPassword(),
                userProfile.isAccess(),
                true,
                true,
                userProfile.isAccess(), mapRolesToAuthorities(userProfile.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }
}
