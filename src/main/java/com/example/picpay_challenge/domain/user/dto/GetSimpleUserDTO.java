package com.example.picpay_challenge.domain.user.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record GetSimpleUserDTO(
    UUID id,
    String fullName,
    BigDecimal balance
) {
}
