package com.example.picpay_challenge.service;

import com.example.picpay_challenge.domain.user.User;
import com.example.picpay_challenge.domain.user.UserRepository;
import com.example.picpay_challenge.domain.user.UserRole;
import com.example.picpay_challenge.domain.user.dto.CreateUserDTO;
import com.example.picpay_challenge.domain.user.dto.GetSimpleUserDTO;
import com.example.picpay_challenge.infra.exceptions.DocumentAlreadyExistsException;
import com.example.picpay_challenge.infra.exceptions.DocumentNotValidException;
import com.example.picpay_challenge.infra.exceptions.EmailAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    public GetSimpleUserDTO create(CreateUserDTO userData) {
        validateDocument(userData.document(), userData.role());
        validateEmail(userData.email());

        User user = new User(userData);
        String encryptedPassword = new BCryptPasswordEncoder().encode(userData.password());
        user.setPassword(encryptedPassword);

        User userCreated = userRepository.save(user);
        return new GetSimpleUserDTO(userCreated.getId(), userCreated.getFullName(), userCreated.getBalance());
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

    public List<GetSimpleUserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> new GetSimpleUserDTO(user.getId(), user.getFullName(), user.getBalance()))
                .toList();
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
    }

    public void updateUsers(List<User> usersToUpdate) {
        userRepository.saveAll(usersToUpdate);
    }
}
