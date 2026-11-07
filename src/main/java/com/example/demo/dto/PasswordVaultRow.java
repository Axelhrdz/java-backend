package com.example.demo.dto;

public record PasswordVaultRow(
    String id,
    String title,
    String secret,
    String userId
) {}
