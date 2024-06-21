package com.example.picpay_challenge.domain.user;

import com.example.picpay_challenge.domain.user.dto.CreateUserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fullName;
    private String document;
    private String email;
    private UserRole role;
    private BigDecimal balance;

    public User(CreateUserDTO user) {
        this.id = UUID.randomUUID();
        this.fullName = user.fullName();
        this.document = user.document();
        this.email = user.email();
        this.role = user.role();
        this.balance = user.balance();
    }
}
