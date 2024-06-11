package com.example.picpay_challenge.domain.user.dto;

import com.example.picpay_challenge.domain.user.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record CreateUserDTO(

    @NotBlank(message = "Nome completo não pode ser vazio")
    String fullName,

    @NotNull(message = "CPF não pode ser vazio")
    @CPF
    String cpf,

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "Email inválido")
    String email,

    @NotBlank(message = "Senha não pode ser vazia")
    String password,

    @NotBlank(message = "Tipo de usuário não pode ser vazio")
    UserRole role
) {
}
