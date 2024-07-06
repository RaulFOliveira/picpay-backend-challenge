package com.example.picpay_challenge.infra.security;

import java.util.UUID;

public record TokenData(
        UUID userId,
        String token
) {

}
