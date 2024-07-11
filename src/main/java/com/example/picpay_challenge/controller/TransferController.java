package com.example.picpay_challenge.controller;

import com.example.picpay_challenge.domain.transfer.Transfer;
import com.example.picpay_challenge.domain.transfer.dto.TransferDTO;
import com.example.picpay_challenge.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    private TransferService service;

    @PostMapping
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferDTO transferDTO) {
        service.transfer(transferDTO);
        return ResponseEntity.ok("Transferência realizada com sucesso");
    }

    @GetMapping
    public ResponseEntity<List<Transfer>> getAllTransfers() {
        List<Transfer> transfers = service.getAllTransfers();
        return transfers.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(transfers);
    }
}
