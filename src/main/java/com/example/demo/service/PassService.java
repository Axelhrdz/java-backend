package com.example.demo.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.demo.model.Password;
import com.example.demo.repository.PasswordRepository;
// import com.example.demo.repository.UserRepository;
// import com.example.demo.service.VaultEncryptionService;

import com.example.demo.dto.PassRequest;
import com.example.demo.dto.PasswordVaultRow;
import com.example.demo.dto.EncryptedVaultRow;
import com.example.demo.dto.GeneratedPasswordResponse;

import com.example.demo.exception.PassTitleAlreadyExistsException;

import org.springframework.stereotype.Service;


@Service
public class PassService {
    
    private final PasswordRepository passwordRepository;
    private final VaultEncryptionService vaultEncryptionService;

    public PassService(
        PasswordRepository passwordRepository,
        VaultEncryptionService vaultEncryptionService
    ) {
        this.passwordRepository = passwordRepository;
        this.vaultEncryptionService = vaultEncryptionService;
    }

    public GeneratedPasswordResponse passGenerator(String userId, PassRequest request) {
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


        String passwordId = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        String generatedPassword = token.toString();

        String encryptedSecret = vaultEncryptionService.encrypt(
            generatedPassword,
            userId,
            passwordId
        );

        Password password = new Password(
            passwordId,
            userId,
            request.getTitle(),
            encryptedSecret,
            Instant.now()
        );

        passwordRepository.save(password);


        return new GeneratedPasswordResponse(
            request.getTitle(),
            generatedPassword
        );

        // return passwordRepository.save(password);
    }




    // ----- Get vault of user passwords/secrets
    public List<PasswordVaultRow> GetPasswordsVault (String userId) {

        //get user info

        List<EncryptedVaultRow> secretsVault = passwordRepository.findAllByUserId(userId);
        List<PasswordVaultRow> res = new ArrayList<>();

        //create new password vault decrypting secret
        for(EncryptedVaultRow item : secretsVault) {
            res.add(new PasswordVaultRow(
                item.id(),
                item.title(),
                vaultEncryptionService.decrypt(item.encryptedSecret(), item.userId(), item.id()),
                item.userId()
            ));
        }


        
        return res;
    }
}
