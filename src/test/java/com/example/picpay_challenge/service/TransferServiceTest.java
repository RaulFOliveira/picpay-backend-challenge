package com.example.picpay_challenge.service;

import com.example.picpay_challenge.domain.transfer.Transfer;
import com.example.picpay_challenge.domain.transfer.TransferRepository;
import com.example.picpay_challenge.domain.transfer.dto.TransferDTO;
import com.example.picpay_challenge.domain.user.User;
import com.example.picpay_challenge.domain.user.UserRole;
import com.example.picpay_challenge.domain.user.dto.CreateUserDTO;
import com.example.picpay_challenge.infra.exceptions.InsufficentBalanceException;
import com.example.picpay_challenge.infra.exceptions.TransferNotAllowedException;
import com.example.picpay_challenge.infra.exceptions.TransferNotAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = com.example.picpay_challenge.PicpayChallengeApplication.class)
@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @InjectMocks
    private TransferService service;
    @MockBean
    private UserService userService;
    @MockBean
    private TransferRepository repo;
    @MockBean
    private RestTemplate restTemplate;

    private User payer;
    private User payee;
    @BeforeEach
    public void setUp() {
        CreateUserDTO payerDTO = new CreateUserDTO(
                "John Doe",
                "11012753085",
                "john.doe@example.com",
                "11987654321",
                UserRole.common,
                BigDecimal.valueOf(500)
        );
        CreateUserDTO payeeDTO = new CreateUserDTO(
                "John Doe",
                "1111111111111",
                "john.doe1@example.com",
                "11987654321",
                UserRole.shopkeeper,
                BigDecimal.valueOf(500)
        );
        this.payer = new User(payerDTO);
        this.payee = new User(payeeDTO);

    }

    @Test
    @DisplayName("Deve transferir com sucesso")
    void transfer() {
        UUID payerId = UUID.randomUUID();
        UUID payeeId = UUID.randomUUID();

        when(userService.getUserById(payerId)).thenReturn(payer);
        when(userService.getUserById(payeeId)).thenReturn(payee);

        when(restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class))
                .thenReturn(ResponseEntity.ok(Map.of("status", "success")));
        TransferDTO transferDTO = new TransferDTO(payerId, payeeId, BigDecimal.valueOf(100));

        service.transfer(transferDTO);

        verify(userService, times(2)).getUserById(any());
        verify(userService, times(1)).updateUsers(anyList());
        verify(repo, times(1)).save(any(Transfer.class));
    }

    @Test
    @DisplayName("Não deve transferir por conta de autorização negada")
    void transferNotAuthorized() {
        UUID payerId = UUID.randomUUID();
        UUID payeeId = UUID.randomUUID();

        when(userService.getUserById(payerId)).thenReturn(payer);
        when(userService.getUserById(payeeId)).thenReturn(payee);
        when(restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class))
                .thenReturn(ResponseEntity.ok(Map.of("status", "failed")));

        TransferDTO transferDTO = new TransferDTO(payerId, payeeId, BigDecimal.valueOf(100));

        var response = assertThrows(TransferNotAuthorizedException.class, () -> service.transfer(transferDTO));

        verify(userService, times(2)).getUserById(any());
        verify(userService, times(0)).updateUsers(anyList());
        verify(repo, times(0)).save(any(Transfer.class));

        assertEquals("Autorização de transferência negada", response.getMessage());
    }

    @Test
    @DisplayName("Não deve transferir com saldo insuficiente")
    void transferInsufficientBalance() {
        UUID payerId = UUID.randomUUID();
        UUID payeeId = UUID.randomUUID();

        when(userService.getUserById(payerId)).thenReturn(payer);
        when(userService.getUserById(payeeId)).thenReturn(payee);

        TransferDTO transferDTO = new TransferDTO(payerId, payeeId, BigDecimal.valueOf(1000));

        var response = assertThrows(InsufficentBalanceException.class, () -> service.transfer(transferDTO));

        verify(userService, times(2)).getUserById(any());
        verify(userService, times(0)).updateUsers(anyList());
        verify(repo, times(0)).save(any(Transfer.class));

        assertEquals("Saldo insuficiente para transferência", response.getMessage());
    }

    @Test
    @DisplayName("Não deve transferir sendo lojista")
    void transferShopkeeper() {
        UUID payerId = UUID.randomUUID();
        UUID payeeId = UUID.randomUUID();

        when(userService.getUserById(payerId)).thenReturn(payer);
        when(userService.getUserById(payeeId)).thenReturn(payee);

        TransferDTO transferDTO = new TransferDTO(payeeId, payerId, BigDecimal.valueOf(100));

        var response = assertThrows(TransferNotAllowedException.class, () -> service.transfer(transferDTO));

        verify(userService, times(2)).getUserById(any());
        verify(userService, times(0)).updateUsers(anyList());
        verify(repo, times(0)).save(any(Transfer.class));

        assertEquals("Lojistas não podem realizar transferências", response.getMessage());
    }

    @Test
    @DisplayName("Deve mostrar todas as transferências")
    void getAllTransfers() {
        List<Transfer> transfers = new ArrayList<>();
        transfers.add(new Transfer(payer.getId(), payee.getId(), BigDecimal.valueOf(100)));
        transfers.add(new Transfer(payer.getId(), payee.getId(), BigDecimal.valueOf(200)));
        transfers.add(new Transfer(payer.getId(), payee.getId(), BigDecimal.valueOf(50)));

        when(repo.findAll()).thenReturn(transfers);

        assertEquals(transfers, service.getAllTransfers());
        assertEquals(3, service.getAllTransfers().size());
    }

    @Test
    @DisplayName("Deve retornar a lista de transferências vazia")
    void getAllTransfersEmpty() {
        List<Transfer> transfers = new ArrayList<>();

        when(repo.findAll()).thenReturn(transfers);

        assertEquals(transfers, service.getAllTransfers());
        assertEquals(0, service.getAllTransfers().size());
    }
}