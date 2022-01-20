package com.ncuz.backend.controller;

import com.ncuz.backend.dto.BalanceDTO;
import com.ncuz.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class TransactionController implements TransactionService {

    private final TransactionService transactionService;

    @Override
    @PostMapping("/deposit")
    public ResponseEntity depositBalance(@RequestBody BalanceDTO balanceDTO) {
        return transactionService.depositBalance(balanceDTO);
    }

    @Override
    @PostMapping("/payment")
    public ResponseEntity payment(@RequestBody BalanceDTO balanceDTO) {
        return transactionService.payment(balanceDTO);
    }

    @Override
    @GetMapping("/check-balance")
    public ResponseEntity getBalance() {
        return transactionService.getBalance();
    }
}
