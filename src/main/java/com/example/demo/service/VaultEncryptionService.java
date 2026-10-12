package com.example.demo.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

//encrpting imports
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;






@Service
public class VaultEncryptionService {
    private final SecretKey key;
    private final SecureRandom secureRandom = new SecureRandom();


    public VaultEncryptionService(
        @Value("${vault.key-file}") String keyFile
    ) throws IOException {

        String encodeKey = Files.readString(
            Path.of(keyFile),
            StandardCharsets.UTF_8
        ).strip();


        byte[] keyBytes = Base64.getDecoder().decode(encodeKey);



        //validation
        if (keyBytes.length != 32) {
            throw new IllegalStateException("Vault encryption key must contain exactly 32 bytes");
        }

        this.key = new SecretKeySpec(keyBytes, "AES");
    }



    //encrypt password method
    public String encrypt(
        String plainText,
        String userId,
        String passwordId
    ) {
        if(plainText == null 
            || userId == null || userId.isBlank() 
            || passwordId == null || passwordId.isBlank()) {
            throw new IllegalArgumentException("Password and owner/records IDs are required");
        } 


        try {
            //Generate freshs nonce for encrypion
            byte[] nonce = new byte[12];
            secureRandom.nextBytes(nonce);


            //confgiure AES-GCM
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameters = new GCMParameterSpec(128, nonce);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameters);


            //Bind encrypted value to its owner and records
            String context = "v1:k1:" + userId + ":" + passwordId;
            cipher.updateAAD(context.getBytes(StandardCharsets.UTF_8));


            //Encrypt the password bytes
            byte[] ciphertextWithTag = cipher.doFinal(
                plainText.getBytes(StandardCharsets.UTF_8)
            );


            //Retunr text package suitable for db
            return "v1:k1:"
                + Base64.getEncoder().encodeToString(nonce)
                + ":"
                + Base64.getEncoder().encodeToString(ciphertextWithTag);





        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to encrypt vault secret", e);
        }
    }


    //decrypt password
    public String decrypt(
        String encryptedValue,
        String userId,
        String passwordId
    ) {
        if(encryptedValue == null || encryptedValue.isBlank()
            || userId == null || userId.isBlank()
            || passwordId == null || passwordId.isBlank()
        ) {
            throw new IllegalArgumentException("Encrypted password and owner/recods IDs are required");
        }



        //Separate and validate the stored package.
        String[] parts = encryptedValue.split(":", -1);

        if(parts.length != 4
            || !"v1".equals(parts[0])
            || !"k1".equals(parts[1])
        ) {
            throw new IllegalArgumentException("Unsupported encrypted password format");
        }


        //Recover the original binary values from Base64
        byte[] nonce = Base64.getDecoder().decode(parts[2]);
        byte[] ciphertextWithTag = Base64.getDecoder().decode(parts[3]);


        if(nonce.length != 12 || ciphertextWithTag.length < 16) {
            throw new IllegalArgumentException("Invalid encrypted password data");
        }




        try {
            //configure decryption with the sae key and ononce
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec parameters = new GCMParameterSpec(128, nonce);

            cipher.init(Cipher.DECRYPT_MODE, key, parameters);


            //supply exactly the same context as encryption
            String context = "v1:k1:" + userId + ":" + passwordId;
            cipher.updateAAD(context.getBytes(StandardCharsets.UTF_8));


            //Verify authenticity and recover the password bytes
            byte[] plaintextBytes = cipher.doFinal(ciphertextWithTag);

            return new String(plaintextBytes, StandardCharsets.UTF_8);
        } catch (GeneralSecurityException e) {
            throw new IllegalArgumentException("Unable to decrypt vault secret", e);
        }

    }







}
