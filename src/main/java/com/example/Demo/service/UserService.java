package com.example.Demo.service;

import com.example.Demo.entity.UserProfile;
import com.example.Demo.entity.UserProfile_;
import com.example.Demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void registerNewUser(UserProfile userProfile){


    }

    public void deleteUser(){

    }

}
