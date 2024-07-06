package com.example.picpay_challenge.domain.user;

import com.example.picpay_challenge.domain.user.dto.CreateUserDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String document;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    private BigDecimal balance;

    public User(CreateUserDTO user) {
        this.id = UUID.randomUUID();
        this.fullName = user.fullName();
        this.document = user.document();
        this.email = user.email();
        this.password = user.password();
        this.role = user.role();
        this.balance = user.balance();
    }

    public void debitBalance(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    public void creditBalance(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return switch (this.role) {
            case common -> List.of(new SimpleGrantedAuthority("ROLE_COMMON"));
            case shopkeeper -> List.of(new SimpleGrantedAuthority("ROLE_SHOPKEEPER"));
            default -> throw new IllegalStateException("Valor inválido: " + role);
        };
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
