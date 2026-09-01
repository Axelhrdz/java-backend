package com.example.demo.service;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;


@Service
public class PassService {
    

    public PassService() {

    }

    public String passGenerator() {

        int length = 20;
        String validChars  = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder token = new StringBuilder();

        //Crypto secure random generator
        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int index = secureRandom.nextInt(validChars.length());
            token.append(validChars.charAt(index));
        }


        // System.out.println("---- secure password ----");
        // System.out.println(token);
        

        return token.toString();
    }
}
