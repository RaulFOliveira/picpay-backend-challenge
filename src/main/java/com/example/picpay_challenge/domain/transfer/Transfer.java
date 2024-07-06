package com.example.picpay_challenge.domain.transfer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "transfers")
@Getter
@Setter
@NoArgsConstructor
public class Transfer {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID payerId;

    @Column(nullable = false)
    private UUID payeeId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime payedAt;

    public Transfer(UUID payerId, UUID payeeId, BigDecimal amount) {
        this.id = UUID.randomUUID();
        this.payerId = payerId;
        this.payeeId = payeeId;
        this.amount = amount;
        this.payedAt = LocalDateTime.now();
    }
}
