package com.example.picpay_challenge.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByDocument(String document);

    Optional<User> findByEmail(String email);
}
