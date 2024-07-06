package com.example.picpay_challenge.service;

import com.example.picpay_challenge.domain.transfer.Transfer;
import com.example.picpay_challenge.domain.transfer.TransferRepository;
import com.example.picpay_challenge.domain.transfer.dto.TransferDTO;
import com.example.picpay_challenge.domain.user.User;
import com.example.picpay_challenge.domain.user.UserRole;
import com.example.picpay_challenge.infra.RestTemplateConfig;
import com.example.picpay_challenge.infra.exceptions.InsufficentBalanceException;
import com.example.picpay_challenge.infra.exceptions.TransferNotAllowedException;
import com.example.picpay_challenge.infra.exceptions.TransferNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class TransferService {

    @Autowired
    private TransferRepository transferRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    public void transfer(TransferDTO transferDTO) {
        User payer = userService.getUserById(transferDTO.payerId());
        User payee = userService.getUserById(transferDTO.payeeId());

        validateTransfer(payer, transferDTO.amount());

        transferRepository.save(new Transfer(payer.getId(), payee.getId(), transferDTO.amount()));
        payer.debitBalance(transferDTO.amount());
        payee.creditBalance(transferDTO.amount());

        List<User> usersToUpdate = List.of(payer, payee);
        userService.updateUsers(usersToUpdate);
    }

    private void validateTransfer(User payer, BigDecimal amount) {
        if (payer.getRole() == UserRole.shopkeeper) {
            throw new TransferNotAllowedException("Lojistas não podem realizar transferências");
        }

        if (payer.getBalance().compareTo(amount) < 0) {
            throw new InsufficentBalanceException("Saldo insuficiente para transferência");
        }

        if (!authorizeTransfer()) {
            throw new TransferNotAuthorizedException("Autorização de transferência negada");
        }
    }

    private boolean authorizeTransfer() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    "https://util.devi.tools/api/v2/authorize",
                    Map.class
            );
            if (response.getBody() != null) {
                String message = (String) response.getBody().get("status");
                return response.getStatusCode() == HttpStatus.OK && message.equals("success");
            }

            return false;
        } catch (HttpClientErrorException e) {
            return false;
        }
    }

    public List<Transfer> getAllTransfers() {
        return transferRepository.findAll();
    }
}
