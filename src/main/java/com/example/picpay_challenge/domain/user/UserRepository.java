package com.example.picpay_challenge.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByDocument(String document);

    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
}
