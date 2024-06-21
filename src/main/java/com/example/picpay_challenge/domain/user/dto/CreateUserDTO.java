package com.example.picpay_challenge.domain.user.dto;

import com.example.picpay_challenge.domain.user.UserRole;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateUserDTO(

    @NotBlank(message = "Nome completo não pode ser vazio")
    String fullName,

    @NotBlank(message = "Documento não pode ser vazio")
    String document,

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email inválido")
    String email,

    @NotNull(message = "Tipo de usuário não pode ser vazio")
    @Enumerated
    UserRole role,

    @NotNull(message = "Saldo inicial não pode ser vazio")
    BigDecimal balance
) {
}
