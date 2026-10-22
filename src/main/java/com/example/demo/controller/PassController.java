package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.example.demo.model.Password;
import com.example.demo.dto.PasswordVaultRow;
import com.example.demo.model.User;


import com.example.demo.dto.GeneratedPasswordResponse;
import com.example.demo.dto.PassRequest;
// import com.example.demo.dto.PasswordVaultRow;
import com.example.demo.service.PassService;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/pass")
public class PassController {

    private final PassService passService;
    private final AuthService authService;
    

    public PassController(PassService passService, AuthService authService) {
        this.passService = passService;
        this.authService = authService;
    }


    //endpoints
    @PostMapping("/generate")
    public ResponseEntity<?> generatePassword(@Valid @RequestBody PassRequest request, @RequestAttribute("userId") String userId) {

        GeneratedPasswordResponse result = passService.passGenerator(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body (Map.of(
            "message", "Generate password endpoint",
            "title", request.getTitle(),
            "password", result.getPassword()
        ));
    }


    @GetMapping("/vault")
    public ResponseEntity<?> getSecretsVault(@RequestAttribute("userId") String userId) {

        User user = authService.authMe(userId);

        List<PasswordVaultRow> passServiceTest = passService.GetPasswordsVault(user.getId());

        return ResponseEntity.status(HttpStatus.ACCEPTED)
            .body(Map.of(
                "message", "Get passwords per user working",
                "value", passServiceTest
            ));
        
    }
}
