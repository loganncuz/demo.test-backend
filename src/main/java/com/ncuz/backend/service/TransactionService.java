package com.ncuz.backend.service;

import com.ncuz.backend.dto.BalanceDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;

public interface TransactionService {

    ResponseEntity depositBalance(BalanceDTO balanceDTO);/**Declare your own parameter**/
    ResponseEntity payment(BalanceDTO balanceDTO);/**Declare your own parameter**/
    ResponseEntity getBalance();/**Declare your own parameter**/
}
