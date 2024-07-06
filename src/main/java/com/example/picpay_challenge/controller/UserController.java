package com.example.picpay_challenge.controller;

import com.example.picpay_challenge.domain.user.dto.CreateUserDTO;
import com.example.picpay_challenge.domain.user.dto.GetSimpleUserDTO;
import com.example.picpay_challenge.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<GetSimpleUserDTO> create(@RequestBody @Valid CreateUserDTO userData) {
        GetSimpleUserDTO user = userService.create(userData);
        return ResponseEntity.status(201).body(user);
    }

    @GetMapping
    public ResponseEntity<List<GetSimpleUserDTO>> getAllUsers() {
        List<GetSimpleUserDTO> users = userService.getAllUsers();
        return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
    }
}
