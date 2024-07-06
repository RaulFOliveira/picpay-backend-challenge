package com.example.picpay_challenge.domain.user.dto;

import com.example.picpay_challenge.domain.user.UserRole;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreateUserDTO(

    @NotBlank(message = "Nome completo não pode ser vazio")
    String fullName,

    @NotBlank(message = "Documento não pode ser vazio")
    String document,

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Senha não pode ser vazio")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    String password,

    @NotNull(message = "Tipo de usuário não pode ser vazio")
    @Enumerated
    UserRole role,

    @NotNull(message = "Saldo inicial não pode ser vazio")
    BigDecimal balance
) {
}
