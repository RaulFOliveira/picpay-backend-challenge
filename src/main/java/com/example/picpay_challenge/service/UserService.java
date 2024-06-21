package com.example.picpay_challenge.service;

import com.example.picpay_challenge.domain.user.User;
import com.example.picpay_challenge.domain.user.UserRepository;
import com.example.picpay_challenge.domain.user.UserRole;
import com.example.picpay_challenge.domain.user.dto.CreateUserDTO;
import com.example.picpay_challenge.infra.exceptions.DocumentAlreadyExistsException;
import com.example.picpay_challenge.infra.exceptions.DocumentNotValidException;
import com.example.picpay_challenge.infra.exceptions.EmailAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User create(CreateUserDTO userData) {
        validateDocument(userData.document(), userData.role());
        validateEmail(userData.email());

        System.out.println(userData.fullName());
        User user = new User(userData);
        User userCreated = userRepository.save(user);
        return user;
    }

    private void validateDocument(String document, UserRole role) {
        if (role == UserRole.shopkeeper && document.length() != 14) {
            throw new DocumentNotValidException("O CNPJ de uma loja deve ter 14 caracteres.");
        }

        if (role == UserRole.common && document.length() != 11) {
            throw new DocumentNotValidException("O CPF de uma pessoa deve ter 11 caracteres.");
        }

        Optional<User> documentExists = userRepository.findByDocument(document);
        if (documentExists.isPresent()) {
            throw new DocumentAlreadyExistsException("Este documento já foi cadastrado.");
        }
    }

    private void validateEmail(String email) {
        Optional<User> emailExists = userRepository.findByEmail(email);
        if (emailExists.isPresent()) {
            throw new EmailAlreadyExistsException("Este email já foi cadastrado.");
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
