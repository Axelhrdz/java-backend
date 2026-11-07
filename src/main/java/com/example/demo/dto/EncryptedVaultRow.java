package com.example.demo.dto;

public record EncryptedVaultRow (
    String id,
    String title,
    String encryptedSecret,
    String userId
) {}
