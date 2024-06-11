package com.example.picpay_challenge.controller;

import com.example.picpay_challenge.domain.user.dto.CreateUserDTO;
import com.example.picpay_challenge.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String create(CreateUserDTO user) {
        return null;
    }

}
