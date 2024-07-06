package com.example.picpay_challenge.controller;

import com.example.picpay_challenge.domain.user.dto.AuthDTO;
import com.example.picpay_challenge.infra.security.TokenData;
import com.example.picpay_challenge.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping
    public ResponseEntity<TokenData> login(@Valid @RequestBody AuthDTO data) {
        return ResponseEntity.ok(service.login(data));
    }
}
