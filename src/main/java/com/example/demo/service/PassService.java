package com.example.demo.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

import com.example.demo.model.Password;
import com.example.demo.repository.PasswordRepository;
import com.example.demo.dto.PassRequest;
import com.example.demo.exception.PassTitleAlreadyExistsException;

import org.springframework.stereotype.Service;


@Service
public class PassService {
    
    private final PasswordRepository passwordRepository;

    public PassService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public Password passGenerator(String userId, PassRequest request) {
        //valida null/invalid user id
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID is required to generate a password");
        }

        //validate duplicate category
        if(passwordRepository.existsByUserIdAndTitle(userId, request.getTitle())) {
            throw new PassTitleAlreadyExistsException("A title/category for this password already exists, please choose another one");
        }
        

        int length = 20;
        String validChars  = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder token = new StringBuilder();

        //Crypto secure random generator
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(validChars.length());
            token.append(validChars.charAt(index));
        }


        //Generate password object
        Password password = new Password(
            UUID.randomUUID().toString().replace("-", "").substring(0, 4),
            userId,
            request.getTitle(),
            token.toString(),
            Instant.now()
        );


        return passwordRepository.save(password);
    }
}
