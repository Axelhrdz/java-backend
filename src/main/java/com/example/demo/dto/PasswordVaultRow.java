package com.example.demo.dto;

// public class PasswordVaultResponse {
//     private final String userId;
//     private final String[] passVault; //temproary, this should map to an object with vault of passwords
    
//     public PasswordVaultResponse(String userId, String[] passVault) {
//         this.userId = userId;
//         this.passVault = passVault;
//     }


//     //getters
//     public String getUserEmail(){
//         return this.userId;
//     }


//     public String[] getPassVault(){
//         return this.passVault;
//     }
// }

public record PasswordVaultRow(
    String id,
    String title,
    String decryptedSecret,
    String userId
) {}
