package com.example.picpay_challenge.domain.transfer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferDTO(
    @NotNull(message = "O 'payerId' é obrigatório")
    UUID payerId,
    @NotNull(message = "O 'payeeId' é obrigatório")
    UUID payeeId,
    @NotNull(message = "O 'amount' é obrigatório")
    @Positive(message = "O 'amount' deve ser positivo")
    BigDecimal amount
) {
}
