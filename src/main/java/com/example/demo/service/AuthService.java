package com.example.demo.service;


import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;


@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public User register(RegisterRequest request) {

        //Validation
        if (
            request.getName() == null 
            || request.getName().isBlank()
            || request.getEmail() == null
            || request.getEmail().isBlank()
            || request.getPassword() == null
            || request.getPassword().isBlank()
        ) {
            throw new IllegalArgumentException("Invalid request");
        }

        if(userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already registered");
        }


        //Validation passes, create user
        String hashedPassword = encoder.encode(request.getPassword());

        User user = new User(
            UUID.randomUUID().toString(),
            request.getName(),
            request.getEmail(),
            hashedPassword
        );

        return userRepository.save(user);
    }



    public User login(LoginRequest request) {

        //Validation
        if (
            request.getEmail() == null
            || request.getEmail().isBlank()
            || request.getPassword() == null
            || request.getPassword().isBlank()
        ) {
            throw new IllegalArgumentException("Invalid login request");
        }

        //get user by email
        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

            
        //Validate credentials/password
        Boolean passMatches = encoder.matches(request.getPassword(), user.getPassword());
        
        if(!passMatches) {
            throw new IllegalArgumentException("Invalid password or email, please verify");
        }

        return user;
    }   
}
